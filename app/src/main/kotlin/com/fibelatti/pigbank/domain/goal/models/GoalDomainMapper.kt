package com.fibelatti.pigbank.domain.goal.models

import com.fibelatti.pigbank.common.stringAsDate
import com.fibelatti.pigbank.common.sumByFloat
import com.fibelatti.pigbank.common.toNormalizedFloat
import com.fibelatti.pigbank.data.goal.GoalDataModel
import com.fibelatti.pigbank.data.goal.GoalWithSavingsDataModel
import java.util.Date
import javax.inject.Inject

private const val oneHundredPercent = 100F
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
            cost = cost.toNormalizedFloat(),
            deadline = stringAsDate(deadline)
        )
    }

    fun toDomainModel(goalWithSavingsDataModel: GoalWithSavingsDataModel): GoalEntity = with(goalWithSavingsDataModel) {
        val totalSaved = savingsDataModelList.sumByFloat { it.amount }
        val remainingCost = goalDataModel.cost - totalSaved
        val daysUntilDeadline = ((goalDataModel.deadline.time - Date().time) / oneDayInMilliseconds).toInt()
        val percentSaved = (totalSaved / goalDataModel.cost) * oneHundredPercent
        val timeElapsed = Math.max(((Date().time - goalDataModel.creationDate.time) / oneDayInMilliseconds).toInt(), 1)

        GoalEntity(
            description = goalDataModel.description,
            cost = goalDataModel.cost,
            deadline = goalDataModel.deadline,
            id = goalDataModel.id,
            creationDate = goalDataModel.creationDate,
            savings = savingsDataModelList.map { savingsDomainMapper.toDomainModel(it) },
            totalSaved = totalSaved,
            remainingCost = remainingCost,
            percentSaved = percentSaved,
            isAchieved = percentSaved >= oneHundredPercent,
            daysUntilDeadline = daysUntilDeadline,
            isOverdue = daysUntilDeadline < 0,
            timeElapsed = timeElapsed
        )
    }
}
