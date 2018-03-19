package com.fibelatti.pigbank.domain.goal

import com.fibelatti.pigbank.data.goal.SavingsRepositoryContract
import com.fibelatti.pigbank.data.localdatasource.DATABASE_GENERIC_ERROR_MESSAGE
import com.fibelatti.pigbank.domain.goal.models.GoalEntity
import com.fibelatti.pigbank.domain.goal.models.SavingsDomainMapper
import io.reactivex.Single
import javax.inject.Inject

class SaveForGoalUseCase @Inject constructor(
    private val savingsRepositoryContract: SavingsRepositoryContract,
    private val savingsDomainMapper: SavingsDomainMapper
) {
    fun saveForGoal(goal: GoalEntity, amount: Float, shouldSubtract: Boolean): Single<List<Long>> = Single.create {
        val multiplier = if (shouldSubtract) -1 else 1
        val actualAmount = amount * multiplier

        val ids = savingsRepositoryContract.saveSavings(savingsDomainMapper.toDataModel(goal.id, actualAmount))

        if (ids.isNotEmpty()) it.onSuccess(ids) else it.onError(Throwable(DATABASE_GENERIC_ERROR_MESSAGE))
    }
}
