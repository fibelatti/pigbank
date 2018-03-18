package com.fibelatti.pigbank.presentation.addgoal

import com.fibelatti.pigbank.presentation.base.BaseContract
import com.fibelatti.pigbank.presentation.models.GoalPresentationModel

interface AddGoalContract {
    interface Presenter : BaseContract.Presenter<View> {
        fun createGoal(description: String, cost: String, deadline: String)
    }

    interface View : BaseContract.View {
        fun onInvalidDescription(error: String)

        fun onInvalidCost(error: String)

        fun onInvalidDeadline(error: String)

        fun onErrorAddingGoal()

        fun onGoalCreated(goal: GoalPresentationModel)
    }
}
