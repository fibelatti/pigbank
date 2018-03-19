package com.fibelatti.pigbank.di.module.builder

import com.fibelatti.pigbank.di.module.AddGoalModule
import com.fibelatti.pigbank.di.module.AddSavingsModule
import com.fibelatti.pigbank.di.module.PreferencesModule
import com.fibelatti.pigbank.di.scope.LifecycleScope
import com.fibelatti.pigbank.presentation.addgoal.AddGoalDialogFragment
import com.fibelatti.pigbank.presentation.addsavings.AddSavingsDialogFragment
import com.fibelatti.pigbank.presentation.goaldetail.detail.GoalDetailFragment
import com.fibelatti.pigbank.presentation.goaldetail.savingslog.SavingsLogFragment
import com.fibelatti.pigbank.presentation.rateapp.RateAppDialogFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface FragmentBuilderModule {
    @ContributesAndroidInjector
    @LifecycleScope
    fun bindGoalDetailFragment(): GoalDetailFragment

    @ContributesAndroidInjector
    @LifecycleScope
    fun bindSavingsLogFragment(): SavingsLogFragment

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
