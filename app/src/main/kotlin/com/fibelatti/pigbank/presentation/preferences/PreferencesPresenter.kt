package com.fibelatti.pigbank.presentation.preferences

import com.fibelatti.pigbank.domain.userpreferences.UserPreferencesUseCase
import com.fibelatti.pigbank.external.providers.ResourceProvider
import com.fibelatti.pigbank.external.providers.SchedulerProvider
import com.fibelatti.pigbank.presentation.base.BasePresenter
import com.fibelatti.pigbank.presentation.preferences.PreferencesContract.View

class PreferencesPresenter(
    schedulerProvider: SchedulerProvider,
    resourceProvider: ResourceProvider,
    private val userPreferencesUseCase: UserPreferencesUseCase
) : PreferencesContract.Presenter, BasePresenter<PreferencesContract.View>(schedulerProvider, resourceProvider) {

    private var view: PreferencesContract.View? = null

    override fun attachView(view: View) {
        super.attachView(view)
        this.view = view
    }

    override fun requestPreferences() {
        userPreferencesUseCase.getUserPreferences()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .subscribeUntilDetached(
                { view?.updatePreferences(it) },
                { view?.handleError(it.message) }
            )
    }

    override fun toggleCrashReport(value: Boolean) {
        userPreferencesUseCase.updateCrashReportsEnabled(value)
            .flatMap { userPreferencesUseCase.getUserPreferences() }
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .subscribeUntilDetached(
                { view?.updatePreferences(it) },
                { view?.errorUpdatingPreferences() }
            )
    }

    override fun toggleAnalytics(value: Boolean) {
        userPreferencesUseCase.updateAnalyticsEnabled(value)
            .flatMap { userPreferencesUseCase.getUserPreferences() }
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .subscribeUntilDetached(
                { view?.updatePreferences(it) },
                { view?.errorUpdatingPreferences() }
            )
    }

    override fun resetHints() {
        userPreferencesUseCase.resetHints()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .subscribeUntilDetached(
                { view?.alertHintsReset() },
                { view?.handleError(it.message) }
            )
    }

    override fun shareApp() {
        view?.showShareMenu()
    }

    override fun rateApp() {
        view?.showRateMenu()
    }
}
