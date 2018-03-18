package com.fibelatti.pigbank.di.module

import com.fibelatti.pigbank.di.module.GoalsModule.Binder
import com.fibelatti.pigbank.domain.goal.GetGoalUseCase
import com.fibelatti.pigbank.domain.goal.SaveForGoalUseCase
import com.fibelatti.pigbank.domain.userpreferences.UserPreferencesUseCase
import com.fibelatti.pigbank.external.providers.ResourceProvider
import com.fibelatti.pigbank.external.providers.SchedulerProvider
import com.fibelatti.pigbank.presentation.goals.GoalsActivity
import com.fibelatti.pigbank.presentation.goals.GoalsContract
import com.fibelatti.pigbank.presentation.goals.GoalsPresenter
import com.fibelatti.pigbank.presentation.models.GoalPresentationMapper
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module(includes = [Binder::class])
class GoalsModule {
    @Module
    interface Binder {
        @Binds
        fun provideGoalsActivity(goalsActivity: GoalsActivity): GoalsActivity
    }

    @Provides
    fun provideGoalsPresenter(
        schedulerProvider: SchedulerProvider,
        resourceProvider: ResourceProvider,
        goalPresentationMapper: GoalPresentationMapper,
        getGoalsUseCase: GetGoalUseCase,
        saveForGoalUseCase: SaveForGoalUseCase,
        userPreferencesUseCase: UserPreferencesUseCase
    ): GoalsContract.Presenter =
        GoalsPresenter(
            schedulerProvider,
            resourceProvider,
            goalPresentationMapper,
            getGoalsUseCase,
            saveForGoalUseCase,
            userPreferencesUseCase)
}
