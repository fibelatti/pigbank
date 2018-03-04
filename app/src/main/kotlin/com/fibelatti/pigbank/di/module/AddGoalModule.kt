package com.fibelatti.pigbank.di.module

import com.fibelatti.pigbank.domain.goal.AddGoalUseCase
import com.fibelatti.pigbank.domain.goal.GetGoalUseCase
import com.fibelatti.pigbank.domain.goal.ValidateGoalUseCase
import com.fibelatti.pigbank.external.providers.ResourceProvider
import com.fibelatti.pigbank.external.providers.SchedulerProvider
import com.fibelatti.pigbank.presentation.addgoal.AddGoalContract
import com.fibelatti.pigbank.presentation.addgoal.AddGoalPresenter
import dagger.Module
import dagger.Provides

@Module
class AddGoalModule {
    @Provides
    fun provideAddGoalPresenter(schedulerProvider: SchedulerProvider,
                                resourceProvider: ResourceProvider,
                                validateGoalUseCase: ValidateGoalUseCase,
                                addGoalUseCase: AddGoalUseCase,
                                getGoalsUseCase: GetGoalUseCase): AddGoalContract.Presenter =
        AddGoalPresenter(schedulerProvider, resourceProvider, validateGoalUseCase, addGoalUseCase, getGoalsUseCase)
}
