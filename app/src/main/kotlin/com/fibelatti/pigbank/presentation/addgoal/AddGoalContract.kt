package com.fibelatti.pigbank.presentation.addgoal

import com.fibelatti.pigbank.presentation.base.BaseContract
import com.fibelatti.pigbank.presentation.common.ObservableView
import com.fibelatti.pigbank.presentation.models.Goal
import java.util.Date

interface AddGoalContract {
    interface Presenter : BaseContract.Presenter<View>

    interface View : BaseContract.View {
        val goalDeadlineClicked: ObservableView<Unit>

        val createGoalClicked: ObservableView<Triple<String, Float, Date>>
        //endregion

        //region Consumes
        fun showDatePicker()

        fun onGoalCreated(goal: Goal)

        fun onErrorAddingGoal()
        //endregion
    }
}
