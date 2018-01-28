package com.fibelatti.pigbank.presentation.preferences

import com.fibelatti.pigbank.presentation.base.BaseContract
import com.fibelatti.pigbank.presentation.common.ObservableView

interface PreferencesContract {
    interface Presenter : BaseContract.Presenter<View>

    interface View : BaseContract.View {
        //region Produces
        fun toggleCrashReport(): ObservableView<Boolean>

        fun toggleAnalytics(): ObservableView<Boolean>

        fun resethints(): ObservableView<Unit>

        fun shareApp(): ObservableView<Unit>

        fun rateApp(): ObservableView<Unit>

        fun ratingChanged(): ObservableView<Unit>
        //endregion

        //region Consumes
        fun updatePreferences(preferences: Preferences)

        fun alertHintsReset()

        fun showShareMenu()

        fun showRateMenu()

        fun askForEmail()

        fun suggestPlayStore()
        //endregion
    }
}
