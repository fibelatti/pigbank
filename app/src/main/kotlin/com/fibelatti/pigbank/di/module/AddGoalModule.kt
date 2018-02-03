package com.fibelatti.pigbank.di.module

import com.fibelatti.pigbank.domain.goal.AddGoalUseCase
import com.fibelatti.pigbank.presentation.addgoal.AddGoalContract
import com.fibelatti.pigbank.presentation.addgoal.AddGoalPresenter
import com.fibelatti.pigbank.presentation.common.providers.SchedulerProvider
import dagger.Module
import dagger.Provides

@Module
class AddGoalModule {

    @Provides
    fun provideAddGoalPresenter(schedulerProvider: SchedulerProvider,
                                addGoalUseCase: AddGoalUseCase): AddGoalContract.Presenter =
        AddGoalPresenter(schedulerProvider, addGoalUseCase)
}
