package com.fibelatti.pigbank.presentation.base

interface BaseContract {
    interface View {
        fun handleError(errorMessage: String?)
    }

    interface Presenter<in V : View> {
        fun attachView(view: V)

        fun detachView()
    }
}
