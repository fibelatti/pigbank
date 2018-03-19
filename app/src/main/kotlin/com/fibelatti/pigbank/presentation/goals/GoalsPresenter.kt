package com.fibelatti.pigbank.presentation.goals

import com.fibelatti.pigbank.domain.goal.GetGoalUseCase
import com.fibelatti.pigbank.domain.goal.SaveForGoalUseCase
import com.fibelatti.pigbank.domain.userpreferences.UserPreferencesUseCase
import com.fibelatti.pigbank.external.providers.ResourceProvider
import com.fibelatti.pigbank.external.providers.SchedulerProvider
import com.fibelatti.pigbank.presentation.base.BasePresenter
import com.fibelatti.pigbank.presentation.goals.GoalsContract.View
import com.fibelatti.pigbank.presentation.models.GoalPresentationMapper
import com.fibelatti.pigbank.presentation.models.GoalPresentationModel

class GoalsPresenter(
    schedulerProvider: SchedulerProvider,
    resourceProvider: ResourceProvider,
    private val goalPresentationMapper: GoalPresentationMapper,
    private val getGoalsUseCase: GetGoalUseCase,
    private val saveForGoalUseCase: SaveForGoalUseCase,
    private val userPreferencesUseCase: UserPreferencesUseCase
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

    override fun saveToGoal(goal: GoalPresentationModel, amount: Float) {
        saveForGoalUseCase.saveForGoal(
            goal = goalPresentationMapper.toDomainModel(goal),
            amount = amount,
            shouldSubtract = false
        )
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .subscribeUntilDetached(
                { showUpdatedGoals() },
                { view?.handleError(it.message) }
            )
    }

    override fun newGoalAdded(goal: GoalPresentationModel) {
        view?.openGoal(goal)
    }

    override fun goalsUpdated() {
        showUpdatedGoals()
    }

    override fun goalDetails(goal: GoalPresentationModel) {
        view?.openGoal(goal)
    }

    override fun addSavings(goal: GoalPresentationModel) {
        view?.showAddSavingsDialog(goal)
    }

    override fun firstGoalHintDismissed() {
        userPreferencesUseCase.setFirstGoalHintDismissed()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .subscribe()
    }

    override fun quickSaveHintDismissed() {
        userPreferencesUseCase.setQuickSaveHintDismissed()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .subscribe()
    }

    private fun showUpdatedGoals() {
        getGoalsUseCase.getAllGoals()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .subscribeUntilDetached(
                { goals ->
                    checkForHints(goals.size)
                    view?.updateGoals(goals.map { goalPresentationMapper.toPresentationModel(it) })
                },
                { view?.handleError(errorMessage = it.message) }
            )
    }

    private fun checkForHints(goalsQuantity: Int) {
        userPreferencesUseCase.getUserPreferences()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .subscribe(
                { preferences ->
                    when (goalsQuantity) {
                        0 -> if (!preferences.firstGoalHintDismissed) view?.showFirstGoalHint()
                        1 -> if (!preferences.quickSaveHintDismissed) view?.showQuickSaveHint()
                    }
                },
                {}
            )
    }
}
