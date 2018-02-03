package com.fibelatti.pigbank.presentation.addgoal

import com.fibelatti.pigbank.R
import com.fibelatti.pigbank.domain.goal.AddGoalUseCase
import com.fibelatti.pigbank.domain.goal.GetGoalsUseCase
import com.fibelatti.pigbank.presentation.addgoal.AddGoalContract.View
import com.fibelatti.pigbank.presentation.base.BasePresenter
import com.fibelatti.pigbank.presentation.common.providers.ResourceProvider
import com.fibelatti.pigbank.presentation.common.providers.SchedulerProvider
import com.fibelatti.pigbank.presentation.models.Goal

class AddGoalPresenter(
    schedulerProvider: SchedulerProvider,
    resourceProvider: ResourceProvider,
    private val addGoalUseCase: AddGoalUseCase,
    private val getGoalsUseCase: GetGoalsUseCase
) : AddGoalContract.Presenter, BasePresenter<AddGoalContract.View>(schedulerProvider, resourceProvider) {

    override fun bind(view: View) {
        super.bind(view)

        view.goalDeadlineClicked
            .getObservable()
            .subscribeUntilDetached { view.showDatePicker() }

        view.createGoalClicked
            .getObservable()
            .subscribeUntilDetached { addGoal(view, Goal(description = it.first, cost = it.second, deadline = it.third)) }
    }

    private fun addGoal(view: AddGoalContract.View, goal: Goal) {
        addGoalUseCase.addGoal(goal)
            .observeOn(schedulerProvider.io())
            .subscribeOn(schedulerProvider.mainThread())
            .subscribeUntilDetached(
                { getAddedGoal(view, goalId = it) },
                { view.onErrorAddingGoal() }
            )
    }

    private fun getAddedGoal(view: AddGoalContract.View, goalId: Long) {
        getGoalsUseCase.getGoalById(goalId)
            .observeOn(schedulerProvider.io())
            .subscribeOn(schedulerProvider.mainThread())
            .subscribeUntilDetached(
                { view.onGoalCreated(goal = it) },
                { view.handleError(resourceProvider.getString(R.string.generic_msg_error)) }
            )
    }
}
