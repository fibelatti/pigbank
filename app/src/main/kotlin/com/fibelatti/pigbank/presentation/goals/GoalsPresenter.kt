package com.fibelatti.pigbank.presentation.goals

import com.fibelatti.pigbank.domain.goal.GetGoalUseCase
import com.fibelatti.pigbank.domain.goal.SaveForGoalUseCase
import com.fibelatti.pigbank.external.providers.ResourceProvider
import com.fibelatti.pigbank.external.providers.SchedulerProvider
import com.fibelatti.pigbank.presentation.base.BasePresenter
import com.fibelatti.pigbank.presentation.goals.GoalsContract.View
import com.fibelatti.pigbank.presentation.models.Goal

class GoalsPresenter(
    schedulerProvider: SchedulerProvider,
    resourceProvider: ResourceProvider,
    private val getGoalsUseCase: GetGoalUseCase,
    private val saveForGoalUseCase: SaveForGoalUseCase
) : GoalsContract.Presenter, BasePresenter<GoalsContract.View>(schedulerProvider, resourceProvider) {

    private var view: GoalsContract.View? = null

    override fun attachView(view: View) {
        super.attachView(view)
        this.view = view
    }

    override fun preferences() {
        view?.goToPreferences()
    }

    override fun addGoal() {
        view?.createGoal()
    }

    override fun saveToGoal(goal: Goal, amount: Float) {
        saveForGoalUseCase.saveForGoal(goal, amount)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .subscribeUntilDetached(
                { showUpdatedGoals() },
                { view?.handleError(it.message) }
            )
    }

    override fun newGoalAdded(goal: Goal) {
        view?.openGoal(goal)
    }

    override fun goalsUpdated() {
        showUpdatedGoals()
    }

    override fun goalDetails(goal: Goal) {
        view?.openGoal(goal)
    }

    override fun addSavings(goal: Goal) {
        view?.showAddSavingsDialog(goal)
    }

    private fun showUpdatedGoals() {
        getGoalsUseCase
            .getAllGoals()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .subscribeUntilDetached(
                { view?.updateGoals(goals = it) },
                { view?.handleError(errorMessage = it.message) }
            )
    }
}
