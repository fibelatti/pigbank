package com.fibelatti.pigbank.presentation.goals

import com.fibelatti.pigbank.domain.goal.GetGoalsUseCase
import com.fibelatti.pigbank.domain.goal.SaveForGoalUseCase
import com.fibelatti.pigbank.presentation.base.BasePresenter
import com.fibelatti.pigbank.presentation.common.SchedulerProvider
import com.fibelatti.pigbank.presentation.models.Goal

class GoalsPresenter(
    schedulerProvider: SchedulerProvider,
    private val getGoalsUseCase: GetGoalsUseCase,
    private val saveForGoalUseCase: SaveForGoalUseCase
) : GoalsContract.Presenter, BasePresenter<GoalsContract.View>(schedulerProvider) {

    override fun bind(view: GoalsContract.View) {
        super.bind(view)

        showUpdatedGoals(view)

        view.preferencesClicked()
            .getObservable()
            .subscribeUntilDetached({ view.goToPreferences() })

        view.addGoalClicked()
            .getObservable()
            .subscribeUntilDetached { view.createGoal() }

        view.goalClicked()
            .getObservable()
            .subscribeUntilDetached { view.openGoal(goal = it) }

        view.addSavingsClicked()
            .getObservable()
            .subscribeUntilDetached { view.showAddSavingsDialog(goal = it) }

        view.addSavingsToGoal()
            .getObservable()
            .subscribeUntilDetached { saveForGoal(view = view, goal = it.first, amount = it.second) }
    }

    private fun showUpdatedGoals(view: GoalsContract.View) {
        getGoalsUseCase
            .getAllGoals()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .subscribeUntilDetached(
                { view.updateGoals(goals = it) },
                { view.handleError(errorMessage = it.message) }
            )
    }

    private fun saveForGoal(view: GoalsContract.View, goal: Goal, amount: Float) {
        saveForGoalUseCase.saveForGoal(goal, amount)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .subscribeUntilDetached(
                { showUpdatedGoals(view) },
                { view.handleError(it.message) }
            )
    }
}
