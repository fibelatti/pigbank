package com.fibelatti.pigbank.presentation.goaldetail

import com.fibelatti.pigbank.presentation.base.BaseContract
import com.fibelatti.pigbank.presentation.models.GoalPresentationModel

interface GoalDetailContract {
    interface Presenter : BaseContract.Presenter<View> {
        fun goalSet(goal: GoalPresentationModel)

        fun addSavings(goal: GoalPresentationModel)

        fun saveGoal(goal: GoalPresentationModel, description: String, cost: String, deadline: String)

        fun deleteGoal(goal: GoalPresentationModel)

        fun confirmDeletion(goal: GoalPresentationModel)
    }

    interface View : BaseContract.View {
        fun showGoalDetails(goal: GoalPresentationModel)

        fun showGoalAchievedDetails(goal: GoalPresentationModel)

        fun showGoalOverdueDetails(goal: GoalPresentationModel)

        fun showChangesSaved()

        fun showAddSavingsDialog(goal: GoalPresentationModel)

        fun onInvalidDescription(error: String)

        fun onInvalidCost(error: String)

        fun onInvalidDeadline(error: String)

        fun onSaveError()

        fun showDeleteConfirmationDialog(goal: GoalPresentationModel)

        fun onGoalDeleted()

        fun onDeleteError()
    }
}
