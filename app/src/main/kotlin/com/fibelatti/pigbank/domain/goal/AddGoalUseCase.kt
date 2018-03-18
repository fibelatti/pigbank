package com.fibelatti.pigbank.domain.goal

import com.fibelatti.pigbank.data.goal.GoalRepositoryContract
import com.fibelatti.pigbank.data.goal.SavingsDataModel
import com.fibelatti.pigbank.data.goal.SavingsRepositoryContract
import com.fibelatti.pigbank.data.localdatasource.AppDatabase
import com.fibelatti.pigbank.data.localdatasource.DATABASE_GENERIC_ERROR_MESSAGE
import com.fibelatti.pigbank.domain.goal.models.GoalDomainMapper
import com.fibelatti.pigbank.domain.goal.models.GoalEntity
import com.fibelatti.pigbank.domain.goal.models.SavingsDomainMapper
import io.reactivex.Single
import javax.inject.Inject

class AddGoalUseCase @Inject constructor(
    private val database: AppDatabase,
    private val goalRepositoryContract: GoalRepositoryContract,
    private val savingsRepositoryContract: SavingsRepositoryContract,
    private val goalDomainMapper: GoalDomainMapper,
    private val savingsDomainMapper: SavingsDomainMapper
) {
    fun addGoal(goal: GoalEntity): Single<Long> = Single.create {
        var goalId = -1L

        database.runInTransaction({
            goalId = goalRepositoryContract.saveGoal(goalDomainMapper.toDataModel(goal))

            val updatedSavings: Array<SavingsDataModel>? = goal.savings.map { savingsDomainMapper.toDataModel(it) }.toTypedArray()

            updatedSavings?.let { savingsRepositoryContract.saveSavings(*updatedSavings) }
        })

        if (goalId != -1L) it.onSuccess(goalId) else it.onError(Throwable(DATABASE_GENERIC_ERROR_MESSAGE))
    }
}
