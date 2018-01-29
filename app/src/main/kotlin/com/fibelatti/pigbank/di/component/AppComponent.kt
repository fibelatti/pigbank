package com.fibelatti.pigbank.di.component

import com.fibelatti.pigbank.di.module.AppModule
import com.fibelatti.pigbank.di.module.GoalDetailModule
import com.fibelatti.pigbank.di.module.GoalsModule
import com.fibelatti.pigbank.di.module.PreferencesModule
import com.fibelatti.pigbank.di.scope.AppScope
import dagger.Component

@Component(
    modules = [AppModule::class]
)
@AppScope
interface AppComponent {
    fun instantiateComponent(goalsModule: GoalsModule): GoalsComponent

    fun instantiateComponent(goalDetailModule: GoalDetailModule): GoalDetailComponent

    fun instantiateComponent(preferencesModule: PreferencesModule): PreferencesComponent
}
