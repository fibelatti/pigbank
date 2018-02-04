package com.fibelatti.pigbank.presentation.goaldetail

import com.fibelatti.pigbank.presentation.base.BaseContract
import com.fibelatti.pigbank.presentation.common.ObservableView
import com.fibelatti.pigbank.presentation.models.Goal

interface GoalDetailContract {
    interface Presenter : BaseContract.Presenter<View>

    interface View : BaseContract.View {
        val detailViewResumed: ObservableView<Goal>

        val goalDeadlineClicked: ObservableView<Unit>

        val addSavingsToGoalClicked: ObservableView<Goal>

        val addSavingsToGoal: ObservableView<Pair<Goal, Float>>

        val saveGoalClicked: ObservableView<Goal>

        val deleteGoalClicked: ObservableView<Goal>

        val deleteGoalConfirmed: ObservableView<Goal>
        //endregion

        //region Consumes
        fun showGoalDetails(goal: Goal)

        fun showDatePicker()

        fun showAddSavingsDialog(goal: Goal)

        fun onGoalSaved(goal: Goal)

        fun onSaveError()

        fun showDeleteConfirmationDialog(goal: Goal)

        fun onGoalDeleted()

        fun onDeleteError()
        //endregion
    }
}
