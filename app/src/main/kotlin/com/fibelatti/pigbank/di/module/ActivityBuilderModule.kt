package com.fibelatti.pigbank.di.module

import com.fibelatti.pigbank.presentation.goaldetail.GoalDetailActivity
import com.fibelatti.pigbank.presentation.goals.GoalsActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface ActivityBuilderModule {
    @ContributesAndroidInjector(modules = [GoalsModule::class])
    fun bindGoalsActivity(): GoalsActivity

    @ContributesAndroidInjector(modules = [GoalDetailModule::class])
    fun bindGoalDetailActivity(): GoalDetailActivity
}
