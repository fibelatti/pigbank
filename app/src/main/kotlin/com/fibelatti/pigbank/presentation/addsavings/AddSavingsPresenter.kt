package com.fibelatti.pigbank.presentation.addsavings

import com.fibelatti.pigbank.domain.goal.GetGoalUseCase
import com.fibelatti.pigbank.domain.goal.SaveForGoalUseCase
import com.fibelatti.pigbank.external.providers.ResourceProvider
import com.fibelatti.pigbank.external.providers.SchedulerProvider
import com.fibelatti.pigbank.presentation.addsavings.AddSavingsContract.View
import com.fibelatti.pigbank.presentation.base.BasePresenter
import com.fibelatti.pigbank.presentation.models.Goal

class AddSavingsPresenter(
    schedulerProvider: SchedulerProvider,
    resourceProvider: ResourceProvider,
    private val saveForGoalUseCase: SaveForGoalUseCase,
    private val getGoalsUseCase: GetGoalUseCase
) : AddSavingsContract.Presenter, BasePresenter<AddSavingsContract.View>(schedulerProvider, resourceProvider) {

    private var view: AddSavingsContract.View? = null

    override fun attachView(view: View) {
        super.attachView(view)
        this.view = view
    }

    override fun addSavings(goal: Goal, amount: Float) {
        saveForGoalUseCase.saveForGoal(goal, amount)
            .flatMap { getGoalsUseCase.getGoalById(it.first()) }
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .subscribeUntilDetached(
                { view?.onSavingsAdded(goal = it) },
                { view?.onErrorAddingSavings() }
            )
    }
}
