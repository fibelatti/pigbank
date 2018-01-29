package com.fibelatti.pigbank.presentation.goals

import com.fibelatti.pigbank.presentation.base.BaseContract
import com.fibelatti.pigbank.presentation.common.ObservableView
import com.fibelatti.pigbank.presentation.models.Goal

interface GoalsContract {
    interface Presenter : BaseContract.Presenter<View>

    interface View : BaseContract.View {
        //region Produces
        fun preferencesClicked(): ObservableView<Unit>

        fun addGoalClicked(): ObservableView<Unit>

        fun goalClicked(): ObservableView<Goal>

        fun addSavingsClicked(): ObservableView<Pair<Goal, Float>>
        //endregion

        //region Consumes
        fun goToPreferences()

        fun createGoal()

        fun openGoal(goal: Goal)

        fun updateGoals(goals: List<Goal>)
        //endregion
    }
}
