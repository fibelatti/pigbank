package com.fibelatti.pigbank.presentation.preferences

import com.fibelatti.pigbank.domain.userpreferences.UserPreferencesModel
import com.fibelatti.pigbank.presentation.base.BaseContract

interface PreferencesContract {
    interface Presenter : BaseContract.Presenter<View> {
        fun requestPreferences()

        fun toggleCrashReport(value: Boolean)

        fun toggleAnalytics(value: Boolean)

        fun resetHints()

        fun shareApp()

        fun rateApp()
    }

    interface View : BaseContract.View {
        fun showPreferences(userPreferencesModel: UserPreferencesModel)

        fun updatePreferences(userPreferencesModel: UserPreferencesModel)

        fun errorUpdatingPreferences()

        fun alertHintsReset()

        fun showShareMenu()

        fun showRateMenu()
    }
}
