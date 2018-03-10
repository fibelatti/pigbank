package com.fibelatti.pigbank.presentation.goaldetail

import com.fibelatti.pigbank.presentation.base.BaseContract
import com.fibelatti.pigbank.presentation.models.Goal
import com.fibelatti.pigbank.presentation.models.GoalCandidate

interface GoalDetailContract {
    interface Presenter : BaseContract.Presenter<View> {
        fun goalSet(goal: Goal)

        fun editDeadline()

        fun addSavings(goal: Goal)

        fun saveGoal(goal: Goal, goalCandidate: GoalCandidate)

        fun deleteGoal(goal: Goal)

        fun confirmDeletion(goal: Goal)
    }

    interface View : BaseContract.View {
        fun showGoalDetails(goal: Goal)

        fun showGoalAchievedDetails(goal: Goal)

        fun showGoalOverdueDetails(goal: Goal)

        fun showChangesSaved()

        fun showDatePicker()

        fun showAddSavingsDialog(goal: Goal)

        fun onSaveError()

        fun showDeleteConfirmationDialog(goal: Goal)

        fun onGoalDeleted()

        fun onDeleteError()
    }
}
