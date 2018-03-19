package com.fibelatti.pigbank.presentation.models

import com.fibelatti.pigbank.common.toFormattedString
import com.fibelatti.pigbank.common.toNormalizedFloat
import com.fibelatti.pigbank.domain.goal.models.GoalEntity
import java.text.DecimalFormatSymbols
import javax.inject.Inject

private const val oneDayInMilliseconds = 1000 * 60 * 60 * 24
private const val weekDays = 7
private const val monthDays = 30
private const val minRelativeDaysToAlert = 0.10F

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
            savings.map { savingsPresentationMapper.toDomainModel(it) },
            totalSaved.toNormalizedFloat(),
            remainingCost.toNormalizedFloat(),
            percentSaved,
            isAchieved,
            daysUntilDeadline,
            isOverdue,
            timeElapsed
        )
    }

    fun toPresentationModel(goalEntity: GoalEntity): GoalPresentationModel = with(goalEntity) {
        GoalPresentationModel(
            description = description,
            cost = cost.toFormattedString(separator = decimalSeparator),
            deadline = deadline,
            id = id,
            creationDate = creationDate,
            savings = savings.map { savingsPresentationMapper.toPresentationModel(it) },
            totalSaved = totalSaved.toFormattedString(separator = decimalSeparator),
            remainingCost = remainingCost.toFormattedString(separator = decimalSeparator),
            percentSaved = percentSaved,
            isAchieved = isAchieved,
            daysUntilDeadline = daysUntilDeadline,
            isOverdue = isOverdue,
            timeElapsed = timeElapsed,
            emphasizeRemainingDays = shouldEmphasizeRemainingDays(goalEntity),
            suggestedSavingsPerDay = (remainingCost / daysUntilDeadline).toFormattedString(separator = decimalSeparator),
            shouldShowSavingsPerWeek = getSuggestedSavings(goalEntity = this, periodToCompare = weekDays) > 0,
            suggestedSavingsPerWeek = getSuggestedSavings(goalEntity = this, periodToCompare = weekDays).toFormattedString(separator = decimalSeparator),
            shouldShowSavingsPerMonth = getSuggestedSavings(goalEntity = this, periodToCompare = monthDays) > 0,
            suggestedSavingsPerMonth = getSuggestedSavings(goalEntity = this, periodToCompare = monthDays).toFormattedString(separator = decimalSeparator),
            shouldShowActualSavingsPerDay = getActualSavingsPerDay(goalEntity = this) > 0,
            actualSavingsPerDay = getActualSavingsPerDay(goalEntity = this).toFormattedString(separator = decimalSeparator),
            shouldShowActualSavingsPerWeek = getActualSavings(goalEntity = this, periodToCompare = weekDays) > 0,
            actualSavingsPerWeek = getActualSavings(goalEntity = this, periodToCompare = weekDays).toFormattedString(separator = decimalSeparator),
            shouldShowActualSavingsPerMonth = getActualSavings(goalEntity = this, periodToCompare = monthDays) > 0,
            actualSavingsPerMonth = getActualSavings(goalEntity = this, periodToCompare = monthDays).toFormattedString(separator = decimalSeparator)
        )
    }

    private fun shouldEmphasizeRemainingDays(goalEntity: GoalEntity): Boolean =
        when (goalEntity.daysUntilDeadline.toFloat() / ((goalEntity.deadline.time - goalEntity.creationDate.time) / oneDayInMilliseconds).toFloat()) {
            in 0F..minRelativeDaysToAlert -> true
            else -> false
        }

    private fun getSuggestedSavings(goalEntity: GoalEntity, periodToCompare: Int): Float =
        if (goalEntity.daysUntilDeadline >= periodToCompare) {
            goalEntity.remainingCost / (goalEntity.daysUntilDeadline / periodToCompare)
        } else {
            0F
        }

    private fun getActualSavingsPerDay(goalEntity: GoalEntity): Float = goalEntity.totalSaved / goalEntity.timeElapsed

    private fun getActualSavings(goalEntity: GoalEntity, periodToCompare: Int): Float =
        if (goalEntity.timeElapsed > periodToCompare) {
            goalEntity.totalSaved / (goalEntity.timeElapsed / periodToCompare)
        } else {
            0F
        }
}
