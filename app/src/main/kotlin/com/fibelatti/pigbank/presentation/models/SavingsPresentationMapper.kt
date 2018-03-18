package com.fibelatti.pigbank.presentation.models

import com.fibelatti.pigbank.common.toFormattedString
import com.fibelatti.pigbank.common.toNormalizedFloat
import com.fibelatti.pigbank.domain.goal.models.SavingsEntity
import java.text.DecimalFormatSymbols
import javax.inject.Inject

class SavingsPresentationMapper @Inject constructor(decimalFormatSymbols: DecimalFormatSymbols?) {
    private val decimalSeparator: String = decimalFormatSymbols?.decimalSeparator?.toString() ?: "."

    fun toDomainModel(savingsPresentationModel: SavingsPresentationModel): SavingsEntity = with(savingsPresentationModel) {
        SavingsEntity(id, goalId, amount.toNormalizedFloat(), date)
    }

    fun toPresentationModel(savingsEntity: SavingsEntity): SavingsPresentationModel = with(savingsEntity) {
        SavingsPresentationModel(id, goalId, amount.toFormattedString(separator = decimalSeparator), date)
    }
}
