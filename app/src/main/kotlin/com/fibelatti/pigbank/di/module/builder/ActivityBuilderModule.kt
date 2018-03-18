package com.fibelatti.pigbank.di.module.builder

import com.fibelatti.pigbank.di.module.GoalDetailModule
import com.fibelatti.pigbank.di.module.GoalsModule
import com.fibelatti.pigbank.di.module.PreferencesModule
import com.fibelatti.pigbank.di.scope.LifecycleScope
import com.fibelatti.pigbank.presentation.goaldetail.GoalDetailActivity
import com.fibelatti.pigbank.presentation.goals.GoalsActivity
import com.fibelatti.pigbank.presentation.preferences.PreferencesActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface ActivityBuilderModule {
    @ContributesAndroidInjector(modules = [GoalsModule::class])
    @LifecycleScope
    fun bindGoalsActivity(): GoalsActivity

    @ContributesAndroidInjector(modules = [GoalDetailModule::class])
    @LifecycleScope
    fun bindGoalDetailActivity(): GoalDetailActivity

    @ContributesAndroidInjector(modules = [PreferencesModule::class])
    @LifecycleScope
    fun bindPreferencesActivity(): PreferencesActivity
}
