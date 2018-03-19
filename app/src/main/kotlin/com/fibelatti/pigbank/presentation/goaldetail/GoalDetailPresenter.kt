package com.fibelatti.pigbank.presentation.goaldetail

import com.fibelatti.pigbank.R
import com.fibelatti.pigbank.domain.goal.AddGoalUseCase
import com.fibelatti.pigbank.domain.goal.DeleteGoalUseCase
import com.fibelatti.pigbank.domain.goal.GetGoalUseCase
import com.fibelatti.pigbank.domain.goal.GoalValidationError
import com.fibelatti.pigbank.domain.goal.InvalidGoalField.COST
import com.fibelatti.pigbank.domain.goal.InvalidGoalField.DEADLINE
import com.fibelatti.pigbank.domain.goal.InvalidGoalField.DESCRIPTION
import com.fibelatti.pigbank.domain.goal.InvalidGoalField.INVALID_DEADLINE
import com.fibelatti.pigbank.domain.goal.InvalidGoalField.PAST_DEADLINE
import com.fibelatti.pigbank.domain.goal.ValidateGoalUseCase
import com.fibelatti.pigbank.domain.goal.models.GoalDomainMapper
import com.fibelatti.pigbank.domain.goal.models.GoalEntity
import com.fibelatti.pigbank.external.providers.ResourceProvider
import com.fibelatti.pigbank.external.providers.SchedulerProvider
import com.fibelatti.pigbank.presentation.base.BasePresenter
import com.fibelatti.pigbank.presentation.goaldetail.GoalDetailContract.View
import com.fibelatti.pigbank.presentation.models.GoalPresentationMapper
import com.fibelatti.pigbank.presentation.models.GoalPresentationModel
import java.util.Date

class GoalDetailPresenter(
    schedulerProvider: SchedulerProvider,
    resourceProvider: ResourceProvider,
    private val goalDomainMapper: GoalDomainMapper,
    private val goalPresentationMapper: GoalPresentationMapper,
    private val getGoalsUseCase: GetGoalUseCase,
    private val validateGoalUseCase: ValidateGoalUseCase,
    private val addGoalUseCase: AddGoalUseCase,
    private val deleteGoalUseCase: DeleteGoalUseCase
) : GoalDetailContract.Presenter, BasePresenter<GoalDetailContract.View>(schedulerProvider, resourceProvider) {

    private var view: GoalDetailContract.View? = null

    override fun attachView(view: View) {
        super.attachView(view)
        this.view = view
    }

    override fun goalSet(goal: GoalPresentationModel) {
        getGoalsUseCase.getGoalById(id = goal.id)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .subscribeUntilDetached { showGoalDetails(it) }
    }

    override fun addSavings(goal: GoalPresentationModel) {
        view?.showAddSavingsDialog(goal)
    }

    override fun removeSavings(goal: GoalPresentationModel) {
        view?.showRemoveSavingsDialog(goal)
    }

    override fun saveGoal(goal: GoalPresentationModel, description: String, cost: String, deadline: String) {
        validateGoalUseCase.validateGoal(
            originalGoal = goalPresentationMapper.toDomainModel(goal),
            goalCandidate = goalDomainMapper.toDomainModel(description, cost, deadline),
            now = Date()
        )
            .flatMap { addGoalUseCase.addGoal(goal = it) }
            .flatMap { getGoalsUseCase.getGoalById(id = it) }
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .subscribeUntilDetached(
                { updatedGoal ->
                    view?.showChangesSaved()

                    showGoalDetails(updatedGoal)
                },
                { throwable ->
                    when (throwable) {
                        is GoalValidationError -> handleValidationError(throwable)
                        else -> view?.onSaveError()
                    }
                }
            )
    }

    override fun deleteGoal(goal: GoalPresentationModel) {
        view?.showDeleteConfirmationDialog(goal)
    }

    override fun confirmDeletion(goal: GoalPresentationModel) {
        deleteGoalUseCase.deleteGoal(goalPresentationMapper.toDomainModel(goal))
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .subscribeUntilDetached(
                {
                    view?.onGoalDeleted()
                },
                { view?.onDeleteError() }
            )
    }

    private fun handleValidationError(error: GoalValidationError) {
        when (error.field) {
            DESCRIPTION -> view?.onInvalidDescription(resourceProvider.getString(R.string.goal_add_invalid_description))
            COST -> view?.onInvalidCost(resourceProvider.getString(R.string.goal_add_invalid_cost))
            DEADLINE -> view?.onInvalidDeadline(resourceProvider.getString(R.string.goal_add_empty_deadline))
            INVALID_DEADLINE -> view?.onInvalidDeadline(resourceProvider.getString(R.string.goal_add_invalid_deadline))
            PAST_DEADLINE -> view?.onInvalidDeadline(resourceProvider.getString(R.string.goal_add_past_deadline))
        }
    }

    private fun showGoalDetails(goalEntity: GoalEntity) {
        val presentationGoal = goalPresentationMapper.toPresentationModel(goalEntity)

        when {
            presentationGoal.isAchieved -> view?.showGoalAchievedDetails(presentationGoal)
            presentationGoal.isOverdue -> view?.showGoalOverdueDetails(presentationGoal)
            else -> view?.showGoalDetails(presentationGoal)
        }
    }
}
