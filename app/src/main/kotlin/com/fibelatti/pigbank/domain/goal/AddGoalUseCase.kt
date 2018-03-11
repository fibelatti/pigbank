package com.fibelatti.pigbank.domain.goal

import com.fibelatti.pigbank.data.goal.GoalRepositoryContract
import com.fibelatti.pigbank.data.goal.Savings
import com.fibelatti.pigbank.data.goal.SavingsRepositoryContract
import com.fibelatti.pigbank.data.localdatasource.AppDatabase
import com.fibelatti.pigbank.data.localdatasource.DATABASE_GENERIC_ERROR_MESSAGE
import com.fibelatti.pigbank.presentation.models.Goal
import io.reactivex.Single
import javax.inject.Inject

class AddGoalUseCase @Inject constructor(
    private val database: AppDatabase,
    private val goalRepositoryContract: GoalRepositoryContract,
    private val savingsRepositoryContract: SavingsRepositoryContract,
    private val goalMapper: GoalMapper,
    private val savingsMapper: SavingsMapper
) {
    fun addGoal(goal: Goal): Single<Long> = Single.create {
        var goalId = -1L

        database.runInTransaction({
            goalId = goalRepositoryContract.saveGoal(goalMapper.toDataModel(goal))

            val updatedSavings: Array<Savings>? = goal.savings.map { savingsMapper.toDataModel(it) }.toTypedArray()

            updatedSavings?.let { savingsRepositoryContract.saveSavings(*updatedSavings) }
        })

        if (goalId != -1L) it.onSuccess(goalId) else it.onError(Throwable(DATABASE_GENERIC_ERROR_MESSAGE))
    }
}
