package com.fibelatti.pigbank.di.module

import com.fibelatti.pigbank.domain.goal.AddGoalUseCase
import com.fibelatti.pigbank.domain.goal.DeleteGoalUseCase
import com.fibelatti.pigbank.domain.goal.GetGoalUseCase
import com.fibelatti.pigbank.domain.goal.SaveForGoalUseCase
import com.fibelatti.pigbank.domain.goal.ValidateGoalUseCase
import com.fibelatti.pigbank.external.providers.ResourceProvider
import com.fibelatti.pigbank.external.providers.SchedulerProvider
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
                                   getGoalsUseCase: GetGoalUseCase,
                                   validateGoalUseCase: ValidateGoalUseCase,
                                   addGoalUseCase: AddGoalUseCase,
                                   deleteGoalUseCase: DeleteGoalUseCase,
                                   saveForGoalUseCase: SaveForGoalUseCase): GoalDetailContract.Presenter =
        GoalDetailPresenter(schedulerProvider, resourceProvider, getGoalsUseCase, validateGoalUseCase, addGoalUseCase, deleteGoalUseCase, saveForGoalUseCase)

    @Provides
    fun provideSavingsAdapter(savingsDelegateAdapter: SavingsDelegateAdapter) =
        SavingsAdapter(savingsDelegateAdapter)

    @Provides
    fun provideSavingsDelegateAdapter() = SavingsDelegateAdapter()
}
