package com.fibelatti.pigbank.presentation.goals

import com.fibelatti.pigbank.presentation.base.BaseContract
import com.fibelatti.pigbank.presentation.models.Goal

interface GoalsContract {
    interface Presenter : BaseContract.Presenter<View> {
        fun preferences()

        fun addGoal()

        fun saveToGoal(goal: Goal, amount: Float)

        fun newGoalAdded(goal: Goal)

        fun goalsUpdated()

        fun goalDetails(goal: Goal)

        fun addSavings(goal: Goal)

        fun firstGoalHintDismissed()

        fun quickSaveHintDismissed()
    }

    interface View : BaseContract.View {
        fun goToPreferences()

        fun createGoal()

        fun openGoal(goal: Goal)

        fun showAddSavingsDialog(goal: Goal)

        fun updateGoals(goals: List<Goal>)

        fun showFirstGoalHint()

        fun showQuickSaveHint()
    }
}
