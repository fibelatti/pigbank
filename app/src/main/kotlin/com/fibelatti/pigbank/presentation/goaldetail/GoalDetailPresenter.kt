package com.fibelatti.pigbank.presentation.goaldetail

import com.fibelatti.pigbank.domain.goal.AddGoalUseCase
import com.fibelatti.pigbank.domain.goal.DeleteGoalUseCase
import com.fibelatti.pigbank.domain.goal.GetGoalUseCase
import com.fibelatti.pigbank.domain.goal.ValidateGoalUseCase
import com.fibelatti.pigbank.external.providers.ResourceProvider
import com.fibelatti.pigbank.external.providers.SchedulerProvider
import com.fibelatti.pigbank.presentation.base.BasePresenter
import com.fibelatti.pigbank.presentation.goaldetail.GoalDetailContract.View
import com.fibelatti.pigbank.presentation.models.Goal
import com.fibelatti.pigbank.presentation.models.GoalCandidate
import java.util.Date

class GoalDetailPresenter(
    schedulerProvider: SchedulerProvider,
    resourceProvider: ResourceProvider,
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

    override fun goalSet(goal: Goal) {
        getGoalsUseCase.getGoalById(id = goal.id)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .subscribeUntilDetached { updatedGoal ->
                when {
                    updatedGoal.isAchieved -> view?.showGoalAchievedDetails(updatedGoal)
                    updatedGoal.isOverdue -> view?.showGoalOverdueDetails(updatedGoal)
                    else -> view?.showGoalDetails(updatedGoal)
                }
            }
    }

    override fun editDeadline() {
        view?.showDatePicker()
    }

    override fun addSavings(goal: Goal) {
        view?.showAddSavingsDialog(goal)
    }

    override fun saveGoal(goal: Goal, goalCandidate: GoalCandidate) {
        validateGoalUseCase.validateGoal(originalGoal = goal, goalCandidate = goalCandidate, now = Date())
            .flatMap { addGoalUseCase.addGoal(goal = it) }
            .flatMap { getGoalsUseCase.getGoalById(id = it) }
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .subscribeUntilDetached(
                { updatedGoal ->
                    view?.showChangesSaved()

                    when {
                        updatedGoal.isAchieved -> view?.showGoalAchievedDetails(updatedGoal)
                        updatedGoal.isOverdue -> view?.showGoalOverdueDetails(updatedGoal)
                        else -> view?.showGoalDetails(updatedGoal)
                    }
                },
                { view?.onSaveError() }
            )
    }

    override fun deleteGoal(goal: Goal) {
        view?.showDeleteConfirmationDialog(goal)
    }

    override fun confirmDeletion(goal: Goal) {
        deleteGoalUseCase.deleteGoal(goal)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .subscribeUntilDetached(
                { view?.onGoalDeleted() },
                { view?.onDeleteError() }
            )
    }
}
