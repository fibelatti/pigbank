package com.fibelatti.pigbank.domain.goal

import com.fibelatti.pigbank.data.goal.GoalRepositoryContract
import com.fibelatti.pigbank.data.goal.GoalWithSavings
import com.fibelatti.pigbank.presentation.models.Goal
import io.reactivex.Single
import javax.inject.Inject

class GetGoalUseCase @Inject constructor(
    private val goalRepositoryContract: GoalRepositoryContract,
    private val goalMapper: GoalMapper
) {
    fun getAllGoals(): Single<List<Goal>> =
        goalRepositoryContract
            .getAllGoals()
            .onErrorReturn { emptyList() }
            .flattenAsObservable<GoalWithSavings> { list -> list }
            .map { goalMapper.toPresentationModel(goalWithSavings = it) }
            .toSortedList { goal1, goal2 -> (goal1.daysUntilDeadline - goal2.daysUntilDeadline).toInt() }

    fun getGoalById(id: Long): Single<Goal> =
        goalRepositoryContract
            .getGoalById(id)
            .map { goalMapper.toPresentationModel(goalWithSavings = it) }
}
