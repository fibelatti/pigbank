package com.fibelatti.pigbank.presentation.models

import com.fibelatti.pigbank.domain.goal.models.SavingsEntity
import javax.inject.Inject

class SavingsPresentationMapper @Inject constructor() {
    fun toDomainModel(savingsPresentationModel: SavingsPresentationModel): SavingsEntity = with(savingsPresentationModel) {
        SavingsEntity(id, goalId, amount, date)
    }

    fun toPresentationModel(savingsEntity: SavingsEntity): SavingsPresentationModel = with(savingsEntity) {
        SavingsPresentationModel(id, goalId, amount, date)
    }
}
