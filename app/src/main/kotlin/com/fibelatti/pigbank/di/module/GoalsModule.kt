package com.fibelatti.pigbank.di.module

import android.content.Context
import android.support.v4.app.FragmentActivity
import com.fibelatti.pigbank.domain.goal.GetGoalsUseCase
import com.fibelatti.pigbank.domain.goal.SaveForGoalUseCase
import com.fibelatti.pigbank.presentation.common.SchedulerProvider
import com.fibelatti.pigbank.presentation.goals.GoalsContract
import com.fibelatti.pigbank.presentation.goals.GoalsPresenter
import dagger.Module
import dagger.Provides

@Module
class GoalsModule(private val activity: FragmentActivity) {

    @Provides
    fun provideContext(): Context = activity.baseContext

    @Provides
    fun provideActivity(): FragmentActivity = activity

    @Provides
    fun provideGoalsPresenter(schedulerProvider: SchedulerProvider,
                              getGoalsUseCase: GetGoalsUseCase,
                              saveForGoalUseCase: SaveForGoalUseCase): GoalsContract.Presenter =
        GoalsPresenter(schedulerProvider, getGoalsUseCase, saveForGoalUseCase)
}
