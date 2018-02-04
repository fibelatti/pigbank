package com.fibelatti.pigbank.presentation.goaldetail

import com.fibelatti.pigbank.domain.goal.AddGoalUseCase
import com.fibelatti.pigbank.domain.goal.DeleteGoalUseCase
import com.fibelatti.pigbank.domain.goal.GetGoalsUseCase
import com.fibelatti.pigbank.domain.goal.SaveForGoalUseCase
import com.fibelatti.pigbank.presentation.base.BasePresenter
import com.fibelatti.pigbank.presentation.common.providers.ResourceProvider
import com.fibelatti.pigbank.presentation.common.providers.SchedulerProvider
import io.reactivex.Observable

class GoalDetailPresenter(
    schedulerProvider: SchedulerProvider,
    resourceProvider: ResourceProvider,
    private val getGoalsUseCase: GetGoalsUseCase,
    private val addGoalUseCase: AddGoalUseCase,
    private val deleteGoalUseCase: DeleteGoalUseCase,
    private val saveForGoalUseCase: SaveForGoalUseCase
) : GoalDetailContract.Presenter, BasePresenter<GoalDetailContract.View>(schedulerProvider, resourceProvider) {

    override fun bind(view: GoalDetailContract.View) {
        super.bind(view)

        view.detailViewResumed
            .getObservable()
            .subscribeUntilDetached { view.showGoalDetails(goal = it) }
        view.goalDeadlineClicked
            .getObservable()
            .subscribeUntilDetached({ view.showDatePicker() })
        view.addSavingsToGoalClicked
            .getObservable()
            .subscribeUntilDetached { view.showAddSavingsDialog(goal = it) }
        view.addSavingsToGoal
            .getObservable()
            .observeOn(schedulerProvider.io())
            .flatMap {
                saveForGoalUseCase.saveForGoal(goal = it.first, amount = it.second)
                return@flatMap Observable.just(it.first)
            }
            .flatMap { getGoalsUseCase.getGoalById(it.id).toObservable() }
            .observeOn(schedulerProvider.mainThread())
            .subscribeUntilDetached(
                { view.onGoalSaved(goal = it) },
                { view.onSaveError() }
            )
        view.saveGoalClicked
            .getObservable()
            .observeOn(schedulerProvider.io())
            .flatMap { addGoalUseCase.addGoal(goal = it).toObservable() }
            .flatMap { goalId -> getGoalsUseCase.getGoalById(goalId).toObservable() }
            .observeOn(schedulerProvider.mainThread())
            .subscribeUntilDetached(
                { view.onGoalSaved(goal = it) },
                { view.onSaveError() }
            )
        view.deleteGoalClicked
            .getObservable()
            .subscribeUntilDetached { view.showDeleteConfirmationDialog(goal = it) }
        view.deleteGoalConfirmed
            .getObservable()
            .observeOn(schedulerProvider.io())
            .flatMap { deleteGoalUseCase.deleteGoal(goal = it).toObservable() }
            .observeOn(schedulerProvider.mainThread())
            .subscribeUntilDetached(
                { view.onGoalDeleted() },
                { view.onDeleteError() }
            )
    }
}
