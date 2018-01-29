package com.fibelatti.pigbank.di.module

import android.content.Context
import android.support.v4.app.FragmentActivity
import com.fibelatti.pigbank.domain.goal.GetGoalsUseCase
import com.fibelatti.pigbank.domain.goal.SaveForGoalUseCase
import com.fibelatti.pigbank.presentation.common.SchedulerProvider
import com.fibelatti.pigbank.presentation.goals.GoalsContract
import com.fibelatti.pigbank.presentation.goals.GoalsPresenter
import com.fibelatti.pigbank.presentation.goals.adapter.GoalsAdapter
import com.fibelatti.pigbank.presentation.goals.adapter.GoalsDelegateAdapter
import com.fibelatti.pigbank.presentation.goals.adapter.LoadingDelegateAdapter
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

    @Provides
    fun provideGoalsAdapter(goalsDelegateAdapter: GoalsDelegateAdapter, loadingDelegateAdapter: LoadingDelegateAdapter) =
        GoalsAdapter(goalsDelegateAdapter, loadingDelegateAdapter)

    @Provides
    fun provideGoalsDelegateAdapter() = GoalsDelegateAdapter()

    @Provides
    fun provideLoadingDelegateAdapter() = LoadingDelegateAdapter()
}
