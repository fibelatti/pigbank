package com.fibelatti.pigbank.domain.goal

import com.fibelatti.pigbank.common.stringAsDate
import com.fibelatti.pigbank.common.sumByFloat
import com.fibelatti.pigbank.data.goal.Goal
import com.fibelatti.pigbank.data.goal.GoalWithSavings
import com.fibelatti.pigbank.presentation.models.GoalCandidate
import java.util.Date
import javax.inject.Inject
import com.fibelatti.pigbank.presentation.models.Goal as PresentationModel

private const val oneHundredPercent = 100F
private const val weekDays = 7
private const val monthDays = 30
private const val minRelativeDaysToAlert = 0.10F
private const val oneDayInMilliseconds = 1000 * 60 * 60 * 24

class GoalMapper @Inject constructor(private val savingsMapper: SavingsMapper) {
    fun toPresentationModel(goalCandidate: GoalCandidate): PresentationModel = with(goalCandidate) {
        PresentationModel(
            description = description,
            cost = cost.toFloat(),
            deadline = stringAsDate(deadline)
        )
    }

    fun toPresentationModel(goalWithSavings: GoalWithSavings) = with(goalWithSavings) {
        val totalSaved = savings.sumByFloat { it.amount }
        val remainingCost = goal.cost - totalSaved
        val daysUntilDeadline = (goal.deadline.time - Date().time) / oneDayInMilliseconds
        val percentSaved = (totalSaved / goal.cost) * oneHundredPercent

        PresentationModel(
            id = goal.id,
            creationDate = goal.creationDate,
            description = goal.description,
            cost = goal.cost,
            totalSaved = totalSaved,
            remainingCost = remainingCost,
            percentSaved = percentSaved,
            isAchieved = percentSaved >= oneHundredPercent,
            deadline = goal.deadline,
            daysUntilDeadline = daysUntilDeadline,
            emphasizeRemainingDays = shouldEmphasizeRemainingDays(goal = goal, daysUntilDeadline = daysUntilDeadline),
            isOverdue = daysUntilDeadline < 0,
            suggestedSavingsPerDay = remainingCost / daysUntilDeadline,
            suggestedSavingsPerWeek = if (daysUntilDeadline >= weekDays) remainingCost / (daysUntilDeadline / weekDays) else 0F,
            suggestedSavingsPerMonth = if (daysUntilDeadline >= monthDays) remainingCost / (daysUntilDeadline / monthDays) else 0F,
            savings = savings.map { savingsMapper.toPresentationModel(it) }
        )
    }

    fun toDataModel(goal: PresentationModel) = with(goal) {
        Goal(id, creationDate, description, cost, deadline)
    }

    private fun shouldEmphasizeRemainingDays(goal: Goal, daysUntilDeadline: Long) =
        when (daysUntilDeadline.toFloat() / ((goal.deadline.time - goal.creationDate.time) / oneDayInMilliseconds).toFloat()) {
            in 0F..minRelativeDaysToAlert -> true
            else -> false
        }
}
