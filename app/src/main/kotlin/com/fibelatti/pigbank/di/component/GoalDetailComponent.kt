package com.fibelatti.pigbank.di.component

import com.fibelatti.pigbank.di.module.GoalDetailModule
import com.fibelatti.pigbank.di.scope.GoalDetailScope
import com.fibelatti.pigbank.presentation.goaldetail.GoalDetailActivity
import dagger.Subcomponent

@Subcomponent(
    modules = [GoalDetailModule::class]
)
@GoalDetailScope
interface GoalDetailComponent {
    fun inject(goalDetailActivity: GoalDetailActivity)
}
