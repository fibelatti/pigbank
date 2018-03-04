package com.fibelatti.pigbank.presentation.preferences

import com.fibelatti.pigbank.presentation.base.BaseContract

interface PreferencesContract {
    interface Presenter : BaseContract.Presenter<View> {
        fun toggleCrashReport(value: Boolean)

        fun toggleAnalytics(value: Boolean)

        fun resethints()

        fun shareApp()

        fun rateApp()

        fun ratingChanged(value: Int)
    }

    interface View : BaseContract.View {
        fun updatePreferences(preferences: Preferences)

        fun alertHintsReset()

        fun showShareMenu()

        fun showRateMenu()

        fun askForEmail()

        fun suggestPlayStore()
    }
}
