package com.fibelatti.pigbank.presentation.goaldetail

import com.fibelatti.pigbank.presentation.base.BaseContract
import com.fibelatti.pigbank.presentation.common.ObservableView
import com.fibelatti.pigbank.presentation.models.Goal
import com.fibelatti.pigbank.presentation.models.Savings

interface GoalDetailContract {
    interface Presenter : BaseContract.Presenter<View>

    interface View : BaseContract.View {
        //region Produces
        fun goalRequested(): ObservableView<Goal>

        fun goalDetailsChanged(): ObservableView<Goal>

        fun addSavings(): ObservableView<Savings>
        //endregion

        //region Consumes
        fun updateGoal(goal: Goal)
        //endregion
    }
}
