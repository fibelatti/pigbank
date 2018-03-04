package com.fibelatti.pigbank.presentation.addgoal

import com.fibelatti.pigbank.presentation.base.BaseContract
import com.fibelatti.pigbank.presentation.models.Goal
import com.fibelatti.pigbank.presentation.models.GoalCandidate

interface AddGoalContract {
    interface Presenter : BaseContract.Presenter<View> {
        fun editDeadline()

        fun createGoals(goal: GoalCandidate)
    }

    interface View : BaseContract.View {
        fun showDatePicker()

        fun onInvalidDescription(error: String)

        fun onInvalidCost(error: String)

        fun onInvalidDeadline(error: String)

        fun onErrorAddingGoal()

        fun onGoalCreated(goal: Goal)
    }
}
