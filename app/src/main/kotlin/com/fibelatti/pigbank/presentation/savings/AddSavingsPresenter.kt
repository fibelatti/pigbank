package com.fibelatti.pigbank.presentation.savings

import com.fibelatti.pigbank.domain.goal.GetGoalUseCase
import com.fibelatti.pigbank.domain.goal.SaveForGoalUseCase
import com.fibelatti.pigbank.domain.goal.SavingsValidationError
import com.fibelatti.pigbank.domain.goal.ValidateSavingsUseCase
import com.fibelatti.pigbank.external.providers.ResourceProvider
import com.fibelatti.pigbank.external.providers.SchedulerProvider
import com.fibelatti.pigbank.presentation.base.BasePresenter
import com.fibelatti.pigbank.presentation.models.GoalPresentationMapper
import com.fibelatti.pigbank.presentation.models.GoalPresentationModel
import com.fibelatti.pigbank.presentation.savings.AddSavingsContract.View

class AddSavingsPresenter(
    schedulerProvider: SchedulerProvider,
    resourceProvider: ResourceProvider,
    private val goalPresentationMapper: GoalPresentationMapper,
    private val validateSavingsUseCase: ValidateSavingsUseCase,
    private val saveForGoalUseCase: SaveForGoalUseCase,
    private val getGoalsUseCase: GetGoalUseCase
) : AddSavingsContract.Presenter, BasePresenter<AddSavingsContract.View>(schedulerProvider, resourceProvider) {

    private var view: AddSavingsContract.View? = null

    override fun attachView(view: View) {
        super.attachView(view)
        this.view = view
    }

    override fun addSavings(goal: GoalPresentationModel, amount: String) {
        validateSavingsUseCase.validateSavings(amount)
            .flatMap { saveForGoalUseCase.saveForGoal(goalPresentationMapper.toDomainModel(goal), it) }
            .flatMap { getGoalsUseCase.getGoalById(goal.id) }
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .subscribeUntilDetached(
                { view?.onSavingsAdded(goal = goalPresentationMapper.toPresentationModel(it)) },
                { throwable ->
                    when (throwable) {
                        is SavingsValidationError -> view?.onInvalidSavingsAmount()
                        else -> view?.onErrorAddingSavings()
                    }
                }
            )
    }
}
