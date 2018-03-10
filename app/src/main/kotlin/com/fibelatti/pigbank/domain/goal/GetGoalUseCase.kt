package com.fibelatti.pigbank.domain.goal

import com.fibelatti.pigbank.data.goal.GoalWithSavings
import com.fibelatti.pigbank.data.localdatasource.AppDatabase
import com.fibelatti.pigbank.presentation.models.Goal
import io.reactivex.Single
import javax.inject.Inject

class GetGoalUseCase @Inject constructor(
    private val database: AppDatabase,
    private val goalMapper: GoalMapper
) {
    fun getAllGoals(): Single<List<Goal>> =
        database.getGoalRepository()
            .getAllGoals()
            .onErrorReturn { emptyList() }
            .flattenAsObservable<GoalWithSavings> { list -> list }
            .map { goalMapper.toPresentationModel(goalWithSavings = it) }
            .toSortedList { goal1, goal2 -> (goal1.daysUntilDeadline - goal2.daysUntilDeadline).toInt() }

    fun getGoalById(id: Long): Single<Goal> =
        database.getGoalRepository()
            .getGoalById(id)
            .map { goalMapper.toPresentationModel(goalWithSavings = it) }
}
