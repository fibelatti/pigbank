package com.fibelatti.pigbank.presentation.goaldetail

import com.fibelatti.pigbank.presentation.base.BaseContract
import com.fibelatti.pigbank.presentation.models.Goal
import com.fibelatti.pigbank.presentation.models.GoalCandidate

interface GoalDetailContract {
    interface Presenter : BaseContract.Presenter<View> {
        fun goalSet(goal: Goal)

        fun editDeadline()

        fun addSavings(goal: Goal)

        fun saveToGoal(goal: Goal, amount: Float)

        fun saveGoal(goal: Goal, goalCandidate: GoalCandidate)

        fun deleteGoal(goal: Goal)

        fun confirmDeletion(goal: Goal)
    }

    interface View : BaseContract.View {
        fun showGoalDetails(goal: Goal)

        fun showDatePicker()

        fun showAddSavingsDialog(goal: Goal)

        fun onGoalSaved(goal: Goal)

        fun onSaveError()

        fun showDeleteConfirmationDialog(goal: Goal)

        fun onGoalDeleted()

        fun onDeleteError()
    }
}
