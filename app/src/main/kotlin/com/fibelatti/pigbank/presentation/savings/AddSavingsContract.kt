package com.fibelatti.pigbank.presentation.savings

import com.fibelatti.pigbank.presentation.base.BaseContract
import com.fibelatti.pigbank.presentation.models.GoalPresentationModel

interface AddSavingsContract {
    interface Presenter : BaseContract.Presenter<View> {
        fun addSavings(goal: GoalPresentationModel, amount: String)
    }

    interface View : BaseContract.View {
        fun onSavingsAdded(goal: GoalPresentationModel)

        fun onInvalidSavingsAmount()

        fun onErrorAddingSavings()
    }
}
