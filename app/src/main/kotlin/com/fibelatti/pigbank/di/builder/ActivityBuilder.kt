package com.fibelatti.pigbank.di.builder

import com.fibelatti.pigbank.di.module.GoalsModule
import com.fibelatti.pigbank.presentation.goals.GoalsActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface ActivityBuilder {
    @ContributesAndroidInjector(modules = [GoalsModule::class])
    fun bindGoalsActivity(): GoalsActivity
}
