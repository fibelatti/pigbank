package com.fibelatti.pigbank.domain.goal.models

import com.fibelatti.pigbank.data.goal.SavingsDataModel
import java.util.Date
import javax.inject.Inject

class SavingsDomainMapper @Inject constructor() {
    fun toDataModel(savings: SavingsEntity): SavingsDataModel = with(savings) {
        val multiplier: Int = if (isRemoval) -1 else 1
        val actualAmount: Float = amount * multiplier

        SavingsDataModel(id, goalId, actualAmount, date)
    }

    fun toDataModel(goalId: Long, amount: Float): SavingsDataModel =
        SavingsDataModel(id = 0, goalId = goalId, amount = amount, date = Date())

    fun toDomainModel(savings: SavingsDataModel): SavingsEntity = with(savings) {
        val isRemoval = amount < 0
        val absoluteAmount = if (isRemoval) amount * -1 else amount

        SavingsEntity(
            id = id,
            goalId = goalId,
            amount = absoluteAmount,
            date = date,
            isRemoval = isRemoval
        )
    }
}
