package com.fibelatti.pigbank.domain.goal

import com.fibelatti.pigbank.common.sumByFloat
import com.fibelatti.pigbank.data.goal.Savings
import com.fibelatti.pigbank.data.localdatasource.AppDatabase
import com.fibelatti.pigbank.data.localdatasource.DATABASE_GENERIC_ERROR_MESSAGE
import com.fibelatti.pigbank.presentation.models.Goal
import io.reactivex.Single
import javax.inject.Inject

class AddGoalUseCase @Inject constructor(private val database: AppDatabase) {
    fun addGoal(goal: Goal): Single<Long> {
        var goalId = -1L

        database.runInTransaction({
            val updatedSum = goal.savings.sumByFloat { it.amount }
            val copy = goal.deepCopy(totalSaved = updatedSum)

            goalId = database.getGoalRepository()
                .saveGoal(GoalMapper.toDataModel(copy))

            val updatedSavings: Array<Savings>? = goal.savings.map { SavingsMapper.toDataModel(it) }.toTypedArray()

            updatedSavings?.let {
                database.getSavingsRepository()
                    .saveSavings(*updatedSavings)
            }
        })

        return if (goalId != -1L) Single.just(goalId) else Single.error(Throwable(DATABASE_GENERIC_ERROR_MESSAGE))
    }
}
