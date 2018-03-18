package com.fibelatti.pigbank.di.module

import com.fibelatti.pigbank.domain.goal.GetGoalUseCase
import com.fibelatti.pigbank.domain.goal.SaveForGoalUseCase
import com.fibelatti.pigbank.domain.goal.ValidateSavingsUseCase
import com.fibelatti.pigbank.external.providers.ResourceProvider
import com.fibelatti.pigbank.external.providers.SchedulerProvider
import com.fibelatti.pigbank.presentation.addsavings.AddSavingsContract
import com.fibelatti.pigbank.presentation.addsavings.AddSavingsPresenter
import com.fibelatti.pigbank.presentation.models.GoalPresentationMapper
import dagger.Module
import dagger.Provides

@Module
class AddSavingsModule {

    @Provides
    fun provideAddSavingsPresenter(
        schedulerProvider: SchedulerProvider,
        resourceProvider: ResourceProvider,
        goalPresentationMapper: GoalPresentationMapper,
        validateSavingsUseCase: ValidateSavingsUseCase,
        saveForGoalUseCase: SaveForGoalUseCase,
        getGoalsUseCase: GetGoalUseCase
    ): AddSavingsContract.Presenter =
        AddSavingsPresenter(
            schedulerProvider,
            resourceProvider,
            goalPresentationMapper,
            validateSavingsUseCase,
            saveForGoalUseCase,
            getGoalsUseCase)
}
