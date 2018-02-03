package com.fibelatti.pigbank.di.module

import com.fibelatti.pigbank.domain.goal.AddGoalUseCase
import com.fibelatti.pigbank.domain.goal.DeleteGoalUseCase
import com.fibelatti.pigbank.domain.goal.GetGoalsUseCase
import com.fibelatti.pigbank.domain.goal.SaveForGoalUseCase
import com.fibelatti.pigbank.presentation.common.providers.ResourceProvider
import com.fibelatti.pigbank.presentation.common.providers.SchedulerProvider
import com.fibelatti.pigbank.presentation.goaldetail.GoalDetailContract
import com.fibelatti.pigbank.presentation.goaldetail.GoalDetailPresenter
import com.fibelatti.pigbank.presentation.goaldetail.adapter.SavingsAdapter
import com.fibelatti.pigbank.presentation.goaldetail.adapter.SavingsDelegateAdapter
import dagger.Module
import dagger.Provides

@Module
class GoalDetailModule {
    @Provides
    fun provideGoalDetailPresenter(schedulerProvider: SchedulerProvider,
                                   resourceProvider: ResourceProvider,
                                   getGoalsUseCase: GetGoalsUseCase,
                                   addGoalUseCase: AddGoalUseCase,
                                   deleteGoalUseCase: DeleteGoalUseCase,
                                   saveForGoalUseCase: SaveForGoalUseCase): GoalDetailContract.Presenter =
        GoalDetailPresenter(schedulerProvider, resourceProvider, getGoalsUseCase, addGoalUseCase, deleteGoalUseCase, saveForGoalUseCase)

    @Provides
    fun provideSavingsAdapter(savingsDelegateAdapter: SavingsDelegateAdapter) =
        SavingsAdapter(savingsDelegateAdapter)

    @Provides
    fun provideSavingsDelegateAdapter() = SavingsDelegateAdapter()
}
