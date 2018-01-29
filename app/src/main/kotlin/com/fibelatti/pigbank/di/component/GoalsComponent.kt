package com.fibelatti.pigbank.di.component

import com.fibelatti.pigbank.di.module.GoalsModule
import com.fibelatti.pigbank.di.scope.GoalsScope
import com.fibelatti.pigbank.presentation.goals.GoalsActivity
import dagger.Subcomponent

@Subcomponent(
    modules = [GoalsModule::class]
)
@GoalsScope
interface GoalsComponent {
    fun inject(goalsActivity: GoalsActivity)
}
