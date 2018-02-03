package com.fibelatti.pigbank.di.module

import com.fibelatti.pigbank.presentation.addgoal.AddGoalDialogFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface FragmentBuilderModule {
    @ContributesAndroidInjector(modules = [AddGoalModule::class])
    fun bindAddGoalDialogFragment(): AddGoalDialogFragment
}
