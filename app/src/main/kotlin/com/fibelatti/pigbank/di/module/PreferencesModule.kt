package com.fibelatti.pigbank.di.module

import com.fibelatti.pigbank.di.module.PreferencesModule.Binder
import com.fibelatti.pigbank.domain.userpreferences.UserPreferencesUseCase
import com.fibelatti.pigbank.external.providers.ResourceProvider
import com.fibelatti.pigbank.external.providers.SchedulerProvider
import com.fibelatti.pigbank.presentation.preferences.PreferencesActivity
import com.fibelatti.pigbank.presentation.preferences.PreferencesContract
import com.fibelatti.pigbank.presentation.preferences.PreferencesPresenter
import com.fibelatti.pigbank.presentation.rateapp.RateAppContract
import com.fibelatti.pigbank.presentation.rateapp.RateAppPresenter
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module(includes = [Binder::class])
class PreferencesModule {

    @Module
    interface Binder {
        @Binds
        fun provideGoalsActivity(preferencesActivity: PreferencesActivity): PreferencesActivity
    }

    @Provides
    fun providePreferencesPresenter(
        schedulerProvider: SchedulerProvider,
        resourceProvider: ResourceProvider,
        userPreferencesUseCase: UserPreferencesUseCase
    ): PreferencesContract.Presenter =
        PreferencesPresenter(schedulerProvider, resourceProvider, userPreferencesUseCase)

    @Provides
    fun provideRateAppPresenter(
        schedulerProvider: SchedulerProvider,
        resourceProvider: ResourceProvider
    ): RateAppContract.Presenter = RateAppPresenter(schedulerProvider, resourceProvider)
}
