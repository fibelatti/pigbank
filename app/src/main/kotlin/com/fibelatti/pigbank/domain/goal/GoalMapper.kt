package com.fibelatti.pigbank.domain.goal

import com.fibelatti.pigbank.data.goal.Goal
import com.fibelatti.pigbank.data.goal.GoalWithSavings
import java.util.Date
import com.fibelatti.pigbank.presentation.models.Goal as PresentationModel

object GoalMapper {
    private const val weekDays = 7
    private const val monthDays = 30
    private const val minRelativeDaysToAlert = 0.10F
    private const val oneDayInMilliseconds = 1000 * 60 * 60 * 24

    fun toPresentationModel(goal: Goal) = with(goal) {
        val remainingCost = cost - savings
        val daysUntilDeadline = (deadline.time - Date().time) / oneDayInMilliseconds

        PresentationModel(
            id = id,
            creationDate = creationDate,
            description = description,
            cost = cost,
            totalSaved = savings,
            remainingCost = remainingCost,
            percentSaved = savings / cost,
            deadline = deadline,
            daysUntilDeadline = daysUntilDeadline,
            emphasizeRemainingDays = shouldEmphasizeRemainingDays(goal = this, daysUntilDeadline = daysUntilDeadline),
            suggestedSavingsPerDay = remainingCost / daysUntilDeadline,
            suggestedSavingsPerWeek = remainingCost / (daysUntilDeadline / weekDays),
            suggestedSavingsPerMonth = remainingCost / (daysUntilDeadline / monthDays),
            savings = emptyList())
    }

    fun toPresentationModel(goalWithSavings: GoalWithSavings) = with(goalWithSavings) {
        val remainingCost = goal.cost - goalWithSavings.goal.savings
        val daysUntilDeadline = goal.deadline.time - Date().time

        PresentationModel(
            id = goal.id,
            creationDate = goal.creationDate,
            description = goal.description,
            cost = goal.cost,
            totalSaved = goal.savings,
            remainingCost = remainingCost,
            percentSaved = goal.savings / goal.cost,
            deadline = goal.deadline,
            daysUntilDeadline = daysUntilDeadline,
            emphasizeRemainingDays = shouldEmphasizeRemainingDays(goal, daysUntilDeadline),
            suggestedSavingsPerDay = remainingCost / daysUntilDeadline,
            suggestedSavingsPerWeek = remainingCost / (daysUntilDeadline / weekDays),
            suggestedSavingsPerMonth = remainingCost / (daysUntilDeadline / monthDays),
            savings = savings.map { SavingsMapper.toPresentationModel(it) }
        )
    }

    fun toDataModel(goal: PresentationModel) = with(goal) {
        Goal(id, creationDate, description, cost, totalSaved, deadline)
    }

    private fun shouldEmphasizeRemainingDays(goal: Goal, daysUntilDeadline: Long) =
        when (daysUntilDeadline.toFloat() / ((goal.deadline.time - goal.creationDate.time) / oneDayInMilliseconds).toFloat()) {
            in 0F..minRelativeDaysToAlert -> true
            else -> false
        }
}
