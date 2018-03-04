package com.fibelatti.pigbank.domain.goal

import com.fibelatti.pigbank.data.goal.Savings
import com.fibelatti.pigbank.data.localdatasource.AppDatabase
import com.fibelatti.pigbank.data.localdatasource.DATABASE_GENERIC_ERROR_MESSAGE
import com.fibelatti.pigbank.presentation.models.Goal
import io.reactivex.Single
import java.util.Date
import javax.inject.Inject

class SaveForGoalUseCase @Inject constructor(private val database: AppDatabase) {
    fun saveForGoal(goal: Goal, amount: Float): Single<List<Long>> = Single.create {
        val ids = database.getSavingsRepository()
            .saveSavings(Savings(id = 0, goalId = goal.id, amount = amount, date = Date()))

        if (ids.isNotEmpty()) it.onSuccess(ids) else it.onError(Throwable(DATABASE_GENERIC_ERROR_MESSAGE))
    }
}
