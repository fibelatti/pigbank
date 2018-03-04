package com.fibelatti.pigbank.domain.goal

import com.fibelatti.pigbank.data.localdatasource.AppDatabase
import com.fibelatti.pigbank.data.localdatasource.DATABASE_GENERIC_ERROR_MESSAGE
import com.fibelatti.pigbank.presentation.models.Goal
import io.reactivex.Single
import javax.inject.Inject

class DeleteGoalUseCase @Inject constructor(private val database: AppDatabase) {
    fun deleteGoal(goal: Goal): Single<Int> = Single.create {
        val affectedRows = database.getGoalRepository().deleteGoalById(goal.id)

        if (affectedRows == 1) it.onSuccess(affectedRows) else it.onError(Throwable(DATABASE_GENERIC_ERROR_MESSAGE))
    }
}
