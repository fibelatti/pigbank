package com.fibelatti.pigbank.di.component

import com.fibelatti.pigbank.di.module.PreferencesModule
import com.fibelatti.pigbank.di.scope.PreferencesScope
import com.fibelatti.pigbank.presentation.preferences.PreferencesActivity
import dagger.Subcomponent

@Subcomponent(
    modules = [PreferencesModule::class]
)
@PreferencesScope
interface PreferencesComponent {
    fun inject(preferencesActivity: PreferencesActivity)
}
