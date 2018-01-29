package com.fibelatti.pigbank.presentation.goals

import com.fibelatti.pigbank.presentation.base.BaseContract
import com.fibelatti.pigbank.presentation.common.ObservableView
import com.fibelatti.pigbank.presentation.models.Goal

interface GoalsContract {
    interface Presenter : BaseContract.Presenter<View>

    interface View : BaseContract.View, Producer, Consumer

    interface Producer {
        fun preferencesClicked(): ObservableView<Unit>

        fun addGoalClicked(): ObservableView<Unit>

        fun goalClicked(): ObservableView<Goal>

        fun addSavingsClicked(): ObservableView<Goal>

        fun addSavingsToGoal(): ObservableView<Pair<Goal, Float>>
    }

    interface Consumer {
        fun goToPreferences()

        fun createGoal()

        fun openGoal(goal: Goal)

        fun showAddSavingsDialog(goal: Goal)

        fun updateGoals(goals: List<Goal>)
    }
}
