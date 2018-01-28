package com.fibelatti.pigbank.domain.goal

import com.fibelatti.pigbank.data.goal.Goal
import com.fibelatti.pigbank.data.goal.GoalWithSavings
import java.util.Date
import com.fibelatti.pigbank.presentation.models.Goal as PresentationModel

object GoalMapper {
    private const val weekDays = 7
    private const val monthDays = 30

    fun toPresentationModel(goal: Goal) = with(goal) {
        val remainingCost = cost - savings
        val daysUntilDeadline = deadline.time - Date().time

        PresentationModel(
            id = id,
            description = description,
            cost = cost,
            totalSaved = savings,
            remainingCost = remainingCost,
            percentSaved = savings / cost,
            deadline = deadline,
            daysUntilDeadline = daysUntilDeadline,
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
            description = goal.description,
            cost = goal.cost,
            totalSaved = goal.savings,
            remainingCost = remainingCost,
            percentSaved = goal.savings / goal.cost,
            deadline = goal.deadline,
            daysUntilDeadline = daysUntilDeadline,
            suggestedSavingsPerDay = remainingCost / daysUntilDeadline,
            suggestedSavingsPerWeek = remainingCost / (daysUntilDeadline / weekDays),
            suggestedSavingsPerMonth = remainingCost / (daysUntilDeadline / monthDays),
            savings = savings.map { SavingsMapper.toPresentationModel(it) }
        )
    }

    fun toDataModel(goal: PresentationModel) = with(goal) {
        Goal(id, description, cost, totalSaved, deadline)
    }
}
