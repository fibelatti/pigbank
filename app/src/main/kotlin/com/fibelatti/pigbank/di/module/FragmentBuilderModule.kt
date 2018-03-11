package com.fibelatti.pigbank.di.module

import com.fibelatti.pigbank.di.scope.LifecycleScope
import com.fibelatti.pigbank.presentation.addgoal.AddGoalDialogFragment
import com.fibelatti.pigbank.presentation.addsavings.AddSavingsDialogFragment
import com.fibelatti.pigbank.presentation.rateapp.RateAppDialogFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface FragmentBuilderModule {
    @ContributesAndroidInjector(modules = [AddGoalModule::class])
    @LifecycleScope
    fun bindAddGoalDialogFragment(): AddGoalDialogFragment

    @ContributesAndroidInjector(modules = [AddSavingsModule::class])
    @LifecycleScope
    fun bindAddSavingsDialogFragment(): AddSavingsDialogFragment

    @ContributesAndroidInjector(modules = [PreferencesModule::class])
    @LifecycleScope
    fun bindRateAppDialogFragment(): RateAppDialogFragment
}
