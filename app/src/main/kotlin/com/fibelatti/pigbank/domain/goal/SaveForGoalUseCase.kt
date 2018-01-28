package com.fibelatti.pigbank.domain.goal

import com.fibelatti.pigbank.data.goal.Savings
import com.fibelatti.pigbank.data.localdatasource.AppDatabase
import com.fibelatti.pigbank.presentation.models.Goal
import io.reactivex.Completable
import java.util.Date
import javax.inject.Inject

class SaveForGoalUseCase @Inject constructor(private val database: AppDatabase) {
    fun saveForGoal(goal: Goal, amount: Float): Completable {
        database.getSavingsRepository()
            .saveSavings(Savings(id = 0, goalId = goal.id, amount = amount, date = Date()))

        return Completable.complete()
    }
}
