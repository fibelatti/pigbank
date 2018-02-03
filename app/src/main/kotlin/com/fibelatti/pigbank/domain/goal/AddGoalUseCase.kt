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

        with(goal) {
            database.runInTransaction({
                val updatedSum = savings.sumByFloat { it.amount }
                val copy = Goal(
                    description,
                    cost,
                    deadline,
                    id,
                    creationDate,
                    updatedSum,
                    remainingCost,
                    percentSaved,
                    daysUntilDeadline,
                    emphasizeRemainingDays,
                    suggestedSavingsPerDay,
                    suggestedSavingsPerWeek,
                    suggestedSavingsPerMonth,
                    savings)

                goalId = database.getGoalRepository()
                    .saveGoal(GoalMapper.toDataModel(copy))

                val updatedSavings: Array<Savings>? = savings.map { SavingsMapper.toDataModel(it) }.toTypedArray()

                updatedSavings?.let {
                    database.getSavingsRepository()
                        .saveSavings(*updatedSavings)
                }
            })
        }

        return if (goalId != -1L) Single.just(goalId) else Single.error(Throwable(DATABASE_GENERIC_ERROR_MESSAGE))
    }
}
