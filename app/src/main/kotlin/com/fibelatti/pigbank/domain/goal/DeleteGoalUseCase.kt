package com.fibelatti.pigbank.domain.goal

import com.fibelatti.pigbank.data.localdatasource.AppDatabase
import com.fibelatti.pigbank.presentation.models.Goal
import io.reactivex.Completable
import javax.inject.Inject

class DeleteGoalUseCase @Inject constructor(private val database: AppDatabase) {
    fun deleteGoal(goal: Goal): Completable {
        database.getGoalRepository().deleteGoalById(goal.id)
        return Completable.complete()
    }
}
