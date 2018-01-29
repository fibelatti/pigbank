package com.fibelatti.pigbank.domain.goal

import com.fibelatti.pigbank.common.sumByFloat
import com.fibelatti.pigbank.data.goal.Savings
import com.fibelatti.pigbank.data.localdatasource.AppDatabase
import com.fibelatti.pigbank.presentation.models.Goal
import io.reactivex.Completable
import javax.inject.Inject

class AddGoalUseCase @Inject constructor(private val database: AppDatabase) {
    fun addGoal(goal: Goal): Completable {
        with(goal) {
            database.runInTransaction({
                val updatedSum = savings.sumByFloat { it.amount }
                val copy = Goal(
                    id,
                    creationDate,
                    description,
                    cost,
                    updatedSum,
                    remainingCost,
                    percentSaved,
                    deadline,
                    daysUntilDeadline,
                    emphasizeRemainingDays,
                    suggestedSavingsPerDay,
                    suggestedSavingsPerWeek,
                    suggestedSavingsPerMonth,
                    savings)

                database.getGoalRepository()
                    .saveGoal(GoalMapper.toDataModel(copy))

                val updatedSavings: Array<Savings> = savings
                    .map { SavingsMapper.toDataModel(it) }.toTypedArray()

                database.getSavingsRepository()
                    .saveSavings(*updatedSavings)
            })
        }

        return Completable.complete()
    }
}
