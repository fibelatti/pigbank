package com.fibelatti.pigbank.di.module

import com.fibelatti.pigbank.domain.goal.AddGoalUseCase
import com.fibelatti.pigbank.domain.goal.DeleteGoalUseCase
import com.fibelatti.pigbank.domain.goal.GetGoalUseCase
import com.fibelatti.pigbank.domain.goal.ValidateGoalUseCase
import com.fibelatti.pigbank.domain.goal.models.GoalDomainMapper
import com.fibelatti.pigbank.external.providers.ResourceProvider
import com.fibelatti.pigbank.external.providers.SchedulerProvider
import com.fibelatti.pigbank.presentation.goaldetail.GoalDetailContract
import com.fibelatti.pigbank.presentation.goaldetail.GoalDetailPresenter
import com.fibelatti.pigbank.presentation.models.GoalPresentationMapper
import dagger.Module
import dagger.Provides

@Module
class GoalDetailModule {
    @Provides
    fun provideGoalDetailPresenter(
        schedulerProvider: SchedulerProvider,
        resourceProvider: ResourceProvider,
        goalDomainMapper: GoalDomainMapper,
        goalPresentationMapper: GoalPresentationMapper,
        getGoalsUseCase: GetGoalUseCase,
        validateGoalUseCase: ValidateGoalUseCase,
        addGoalUseCase: AddGoalUseCase,
        deleteGoalUseCase: DeleteGoalUseCase
    ): GoalDetailContract.Presenter =
        GoalDetailPresenter(
            schedulerProvider,
            resourceProvider,
            goalDomainMapper,
            goalPresentationMapper,
            getGoalsUseCase,
            validateGoalUseCase,
            addGoalUseCase,
            deleteGoalUseCase)
}
