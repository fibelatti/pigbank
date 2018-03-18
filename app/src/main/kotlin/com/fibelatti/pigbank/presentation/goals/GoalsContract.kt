package com.fibelatti.pigbank.presentation.goals

import com.fibelatti.pigbank.presentation.base.BaseContract
import com.fibelatti.pigbank.presentation.models.GoalPresentationModel

interface GoalsContract {
    interface Presenter : BaseContract.Presenter<View> {
        fun preferences()

        fun addGoal()

        fun saveToGoal(goal: GoalPresentationModel, amount: Float)

        fun newGoalAdded(goal: GoalPresentationModel)

        fun goalsUpdated()

        fun goalDetails(goal: GoalPresentationModel)

        fun addSavings(goal: GoalPresentationModel)

        fun firstGoalHintDismissed()

        fun quickSaveHintDismissed()
    }

    interface View : BaseContract.View {
        fun goToPreferences()

        fun createGoal()

        fun openGoal(goal: GoalPresentationModel)

        fun showAddSavingsDialog(goal: GoalPresentationModel)

        fun updateGoals(goals: List<GoalPresentationModel>)

        fun showFirstGoalHint()

        fun showQuickSaveHint()
    }
}
