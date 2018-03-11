package com.fibelatti.pigbank.presentation.addgoal

import com.fibelatti.pigbank.R
import com.fibelatti.pigbank.domain.goal.AddGoalUseCase
import com.fibelatti.pigbank.domain.goal.GetGoalUseCase
import com.fibelatti.pigbank.domain.goal.GoalValidationError
import com.fibelatti.pigbank.domain.goal.InvalidGoalField.COST
import com.fibelatti.pigbank.domain.goal.InvalidGoalField.DEADLINE
import com.fibelatti.pigbank.domain.goal.InvalidGoalField.DESCRIPTION
import com.fibelatti.pigbank.domain.goal.InvalidGoalField.PAST_DEADLINE
import com.fibelatti.pigbank.domain.goal.ValidateGoalUseCase
import com.fibelatti.pigbank.domain.userpreferences.UserPreferencesUseCase
import com.fibelatti.pigbank.external.providers.ResourceProvider
import com.fibelatti.pigbank.external.providers.SchedulerProvider
import com.fibelatti.pigbank.presentation.addgoal.AddGoalContract.View
import com.fibelatti.pigbank.presentation.base.BasePresenter
import com.fibelatti.pigbank.presentation.models.GoalCandidate
import java.util.Date

class AddGoalPresenter(
    schedulerProvider: SchedulerProvider,
    resourceProvider: ResourceProvider,
    private val validateGoalUseCase: ValidateGoalUseCase,
    private val addGoalUseCase: AddGoalUseCase,
    private val getGoalsUseCase: GetGoalUseCase,
    private val userPreferencesUseCase: UserPreferencesUseCase
) : AddGoalContract.Presenter, BasePresenter<AddGoalContract.View>(schedulerProvider, resourceProvider) {

    private var view: AddGoalContract.View? = null

    override fun attachView(view: View) {
        super.attachView(view)
        this.view = view
    }

    override fun editDeadline() {
        view?.showDatePicker()
    }

    override fun createGoal(goal: GoalCandidate) {
        validateGoalUseCase.validateGoal(now = Date(), goalCandidate = goal)
            .flatMap { addGoalUseCase.addGoal(goal = it) }
            .flatMap { getGoalsUseCase.getGoalById(id = it) }
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .subscribeUntilDetached(
                {
                    dismissFirstGoalHint()
                    view?.onGoalCreated(goal = it)
                },
                { throwable ->
                    when (throwable) {
                        is GoalValidationError -> handleValidationError(throwable)
                        else -> view?.onErrorAddingGoal()
                    }
                }
            )
    }

    private fun handleValidationError(error: GoalValidationError) {
        when (error.field) {
            DESCRIPTION -> view?.onInvalidDescription(resourceProvider.getString(R.string.goal_add_invalid_description))
            COST -> view?.onInvalidCost(resourceProvider.getString(R.string.goal_add_invalid_cost))
            DEADLINE -> view?.onInvalidDeadline(resourceProvider.getString(R.string.goal_add_invalid_deadline))
            PAST_DEADLINE -> view?.onInvalidDeadline(resourceProvider.getString(R.string.goal_add_past_deadline))
        }
    }

    private fun dismissFirstGoalHint() {
        userPreferencesUseCase.setFirstGoalHintDismissed()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .subscribe()
    }
}
