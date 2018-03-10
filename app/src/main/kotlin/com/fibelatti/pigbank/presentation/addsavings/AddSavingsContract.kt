package com.fibelatti.pigbank.presentation.addsavings

import com.fibelatti.pigbank.presentation.base.BaseContract
import com.fibelatti.pigbank.presentation.models.Goal

interface AddSavingsContract {
    interface Presenter : BaseContract.Presenter<View> {
        fun addSavings(goal: Goal, amount: String)
    }

    interface View : BaseContract.View {
        fun onSavingsAdded(goal: Goal)

        fun onInvalidSavingsAmount()

        fun onErrorAddingSavings()
    }
}
