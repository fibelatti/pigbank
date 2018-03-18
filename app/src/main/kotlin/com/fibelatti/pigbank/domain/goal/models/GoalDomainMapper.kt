package com.fibelatti.pigbank.domain.goal.models

import com.fibelatti.pigbank.common.stringAsDate
import com.fibelatti.pigbank.common.sumByFloat
import com.fibelatti.pigbank.data.goal.GoalDataModel
import com.fibelatti.pigbank.data.goal.GoalWithSavingsDataModel
import java.util.Date
import javax.inject.Inject

private const val oneHundredPercent = 100F
private const val weekDays = 7
private const val monthDays = 30
private const val minRelativeDaysToAlert = 0.10F
private const val oneDayInMilliseconds = 1000 * 60 * 60 * 24

class GoalDomainMapper @Inject constructor(private val savingsDomainMapper: SavingsDomainMapper) {
    fun toDataModel(goal: GoalEntity) = with(goal) {
        GoalDataModel(id, creationDate, description, cost, deadline)
    }

    fun toDomainModel(description: String, cost: String, deadline: String): GoalCandidateEntity =
        GoalCandidateEntity(description, cost, deadline)

    fun toDomainModel(goalCandidate: GoalCandidateEntity): GoalEntity = with(goalCandidate) {
        GoalEntity(
            description = description,
            cost = cost.toFloat(),
            deadline = stringAsDate(deadline)
        )
    }

    fun toDomainModel(goalWithSavingsDataModel: GoalWithSavingsDataModel): GoalEntity = with(goalWithSavingsDataModel) {
        val totalSaved = savingsDataModelList.sumByFloat { it.amount }
        val remainingCost = goalDataModel.cost - totalSaved
        val daysUntilDeadline = (goalDataModel.deadline.time - Date().time) / oneDayInMilliseconds
        val percentSaved = (totalSaved / goalDataModel.cost) * oneHundredPercent

        GoalEntity(
            id = goalDataModel.id,
            creationDate = goalDataModel.creationDate,
            description = goalDataModel.description,
            cost = goalDataModel.cost,
            totalSaved = totalSaved,
            remainingCost = remainingCost,
            percentSaved = percentSaved,
            isAchieved = percentSaved >= oneHundredPercent,
            deadline = goalDataModel.deadline,
            daysUntilDeadline = daysUntilDeadline,
            emphasizeRemainingDays = shouldEmphasizeRemainingDays(goalDataModel = goalDataModel, daysUntilDeadline = daysUntilDeadline),
            isOverdue = daysUntilDeadline < 0,
            suggestedSavingsPerDay = remainingCost / daysUntilDeadline,
            suggestedSavingsPerWeek = if (daysUntilDeadline >= weekDays) remainingCost / (daysUntilDeadline / weekDays) else 0F,
            suggestedSavingsPerMonth = if (daysUntilDeadline >= monthDays) remainingCost / (daysUntilDeadline / monthDays) else 0F,
            savings = savingsDataModelList.map { savingsDomainMapper.toDomainModel(it) }
        )
    }

    private fun shouldEmphasizeRemainingDays(goalDataModel: GoalDataModel, daysUntilDeadline: Long) =
        when (daysUntilDeadline.toFloat() / ((goalDataModel.deadline.time - goalDataModel.creationDate.time) / oneDayInMilliseconds).toFloat()) {
            in 0F..minRelativeDaysToAlert -> true
            else -> false
        }
}
