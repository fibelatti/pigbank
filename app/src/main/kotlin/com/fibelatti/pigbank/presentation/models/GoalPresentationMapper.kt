package com.fibelatti.pigbank.presentation.models

import com.fibelatti.pigbank.common.toFormattedString
import com.fibelatti.pigbank.common.toNormalizedFloat
import com.fibelatti.pigbank.domain.goal.models.GoalEntity
import java.text.DecimalFormatSymbols
import javax.inject.Inject

private const val FLOAT_SEPARATOR = "."

class GoalPresentationMapper @Inject constructor(
    private val savingsPresentationMapper: SavingsPresentationMapper,
    decimalFormatSymbols: DecimalFormatSymbols?
) {
    private val decimalSeparator: String = decimalFormatSymbols?.decimalSeparator?.toString() ?: "."

    fun toDomainModel(goalPresentationModel: GoalPresentationModel): GoalEntity = with(goalPresentationModel) {
        GoalEntity(
            description,
            cost.toNormalizedFloat(),
            deadline,
            id,
            creationDate,
            totalSaved.toNormalizedFloat(),
            remainingCost.toNormalizedFloat(),
            percentSaved,
            isAchieved,
            daysUntilDeadline,
            emphasizeRemainingDays,
            isOverdue,
            suggestedSavingsPerDay.toNormalizedFloat(),
            suggestedSavingsPerWeek.toNormalizedFloat(),
            suggestedSavingsPerMonth.toNormalizedFloat(),
            savings.map { savingsPresentationMapper.toDomainModel(it) })
    }

    fun toPresentationModel(goalEntity: GoalEntity): GoalPresentationModel = with(goalEntity) {
        GoalPresentationModel(
            description,
            cost.toFormattedString().replace(FLOAT_SEPARATOR, decimalSeparator),
            deadline,
            id,
            creationDate,
            totalSaved.toFormattedString(separator = decimalSeparator),
            remainingCost.toFormattedString(separator = decimalSeparator),
            percentSaved,
            isAchieved,
            daysUntilDeadline,
            emphasizeRemainingDays,
            isOverdue,
            suggestedSavingsPerDay.toFormattedString(separator = decimalSeparator),
            suggestedSavingsPerWeek.toFormattedString(separator = decimalSeparator),
            suggestedSavingsPerMonth.toFormattedString(separator = decimalSeparator),
            savings.map { savingsPresentationMapper.toPresentationModel(it) }
        )
    }
}
