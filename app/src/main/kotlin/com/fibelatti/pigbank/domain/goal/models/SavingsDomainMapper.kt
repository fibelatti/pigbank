package com.fibelatti.pigbank.domain.goal.models

import com.fibelatti.pigbank.data.goal.SavingsDataModel
import java.util.Date
import javax.inject.Inject

class SavingsDomainMapper @Inject constructor() {
    fun toDataModel(savings: SavingsEntity): SavingsDataModel = with(savings) {
        SavingsDataModel(id, goalId, amount, date)
    }

    fun toDataModel(goalId: Long, amount: Float): SavingsDataModel =
        SavingsDataModel(id = 0, goalId = goalId, amount = amount, date = Date())

    fun toDomainModel(savings: SavingsDataModel): SavingsEntity = with(savings) {
        SavingsEntity(id, goalId, amount, date)
    }
}
