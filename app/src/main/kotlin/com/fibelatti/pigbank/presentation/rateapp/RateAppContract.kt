package com.fibelatti.pigbank.presentation.rateapp

import com.fibelatti.pigbank.presentation.base.BaseContract

interface RateAppContract {
    interface Presenter : BaseContract.Presenter<View> {
        fun ratingChanged(value: Int)
    }

    interface View : BaseContract.View {
        fun showPlayStore()

        fun showEmail()
    }
}
