package com.fibelatti.pigbank.di.module

import com.fibelatti.pigbank.domain.goal.AddGoalUseCase
import com.fibelatti.pigbank.domain.goal.GetGoalsUseCase
import com.fibelatti.pigbank.presentation.addgoal.AddGoalContract
import com.fibelatti.pigbank.presentation.addgoal.AddGoalPresenter
import com.fibelatti.pigbank.presentation.common.providers.ResourceProvider
import com.fibelatti.pigbank.presentation.common.providers.SchedulerProvider
import dagger.Module
import dagger.Provides

@Module
class AddGoalModule {

    @Provides
    fun provideAddGoalPresenter(schedulerProvider: SchedulerProvider,
                                resourceProvider: ResourceProvider,
                                addGoalUseCase: AddGoalUseCase,
                                getGoalsUseCase: GetGoalsUseCase): AddGoalContract.Presenter =
        AddGoalPresenter(schedulerProvider, resourceProvider, addGoalUseCase, getGoalsUseCase)
}
