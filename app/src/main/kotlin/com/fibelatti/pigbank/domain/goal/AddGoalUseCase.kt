package com.fibelatti.pigbank.domain.goal

import com.fibelatti.pigbank.data.goal.Savings
import com.fibelatti.pigbank.data.localdatasource.AppDatabase
import com.fibelatti.pigbank.data.localdatasource.DATABASE_GENERIC_ERROR_MESSAGE
import com.fibelatti.pigbank.presentation.models.Goal
import io.reactivex.Single
import javax.inject.Inject

class AddGoalUseCase @Inject constructor(
    private val database: AppDatabase,
    private val goalMapper: GoalMapper,
    private val savingsMapper: SavingsMapper
) {
    fun addGoal(goal: Goal): Single<Long> = Single.create {
        var goalId = -1L

        database.runInTransaction({
            goalId = database.getGoalRepository()
                .saveGoal(goalMapper.toDataModel(goal))

            val updatedSavings: Array<Savings>? = goal.savings.map { savingsMapper.toDataModel(it) }.toTypedArray()

            updatedSavings?.let {
                database.getSavingsRepository()
                    .saveSavings(*updatedSavings)
            }
        })

        if (goalId != -1L) it.onSuccess(goalId) else it.onError(Throwable(DATABASE_GENERIC_ERROR_MESSAGE))
    }
}
