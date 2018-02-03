package com.fibelatti.pigbank.presentation.goals

import com.fibelatti.pigbank.presentation.base.BaseContract
import com.fibelatti.pigbank.presentation.common.ObservableView
import com.fibelatti.pigbank.presentation.models.Goal

interface GoalsContract {
    interface Presenter : BaseContract.Presenter<View>

    interface View : BaseContract.View, Producer, Consumer

    interface Producer {
        val preferencesClicked: ObservableView<Unit>

        val addGoalClicked: ObservableView<Unit>

        val addSavingsToGoal: ObservableView<Pair<Goal, Float>>

        val newGoalAdded: ObservableView<Goal>

        fun goalClicked(): ObservableView<Goal>

        fun addSavingsClicked(): ObservableView<Goal>
    }

    interface Consumer {
        fun goToPreferences()

        fun createGoal()

        fun openGoal(goal: Goal)

        fun showAddSavingsDialog(goal: Goal)

        fun updateGoals(goals: List<Goal>)
    }
}
