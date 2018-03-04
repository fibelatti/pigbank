package com.fibelatti.pigbank.di.module

import com.fibelatti.pigbank.presentation.addgoal.AddGoalDialogFragment
import com.fibelatti.pigbank.presentation.addsavings.AddSavingsDialogFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface FragmentBuilderModule {
    @ContributesAndroidInjector(modules = [AddGoalModule::class])
    fun bindAddGoalDialogFragment(): AddGoalDialogFragment

    @ContributesAndroidInjector(modules = [AddSavingsModule::class])
    fun bindAddSavingsDialogFragment(): AddSavingsDialogFragment
}
