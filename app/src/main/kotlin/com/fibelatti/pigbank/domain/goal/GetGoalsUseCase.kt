package com.fibelatti.pigbank.domain.goal

import com.fibelatti.pigbank.data.localdatasource.AppDatabase
import io.reactivex.Single
import javax.inject.Inject
import com.fibelatti.pigbank.data.goal.Goal as DataModel
import com.fibelatti.pigbank.presentation.models.Goal as PresentationModel

class GetGoalsUseCase @Inject constructor(private val database: AppDatabase) {
    fun getAllGoals(): Single<List<PresentationModel>> =
        database.getGoalRepository()
            .getAllGoals()
            .onErrorReturn { emptyList() }
            .flattenAsObservable<DataModel> { list -> list }
            .map { GoalMapper.toPresentationModel(goal = it) }
            .toSortedList { goal1, goal2 -> (goal1.daysUntilDeadline - goal2.daysUntilDeadline).toInt() }

    fun getGoalById(id: Long): Single<PresentationModel> =
        database.getGoalRepository()
            .getGoalById(id)
            .map { GoalMapper.toPresentationModel(goalWithSavings = it) }
}
