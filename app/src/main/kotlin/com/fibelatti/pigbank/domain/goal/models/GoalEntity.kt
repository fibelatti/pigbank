package com.fibelatti.pigbank.domain.goal.models

import java.util.Date

data class GoalEntity(
    val description: String,
    val cost: Float,
    val deadline: Date,
    val id: Long = 0,
    val creationDate: Date = Date(),
    val totalSaved: Float = 0F,
    val remainingCost: Float = 0F,
    val percentSaved: Float = 0F,
    val isAchieved: Boolean = false,
    val daysUntilDeadline: Long = 0,
    val emphasizeRemainingDays: Boolean = false,
    val isOverdue: Boolean = false,
    val suggestedSavingsPerDay: Float = 0F,
    val suggestedSavingsPerWeek: Float = 0F,
    val suggestedSavingsPerMonth: Float = 0F,
    val savings: List<SavingsEntity> = emptyList()
) {
    fun deepCopy(
        description: String = this.description,
        cost: Float = this.cost,
        deadline: Date = this.deadline,
        id: Long = this.id,
        creationDate: Date = this.creationDate,
        totalSaved: Float = this.totalSaved,
        remainingCost: Float = this.remainingCost,
        percentSaved: Float = this.percentSaved,
        isAchieved: Boolean = this.isOverdue,
        daysUntilDeadline: Long = this.daysUntilDeadline,
        emphasizeRemainingDays: Boolean = this.emphasizeRemainingDays,
        isOverdue: Boolean = this.isOverdue,
        suggestedSavingsPerDay: Float = this.suggestedSavingsPerDay,
        suggestedSavingsPerWeek: Float = this.suggestedSavingsPerWeek,
        suggestedSavingsPerMonth: Float = this.suggestedSavingsPerMonth,
        savings: List<SavingsEntity> = this.savings.map { it.copy() }
    ) = GoalEntity(description, cost, deadline, id, creationDate, totalSaved, remainingCost, percentSaved,
        isAchieved, daysUntilDeadline, emphasizeRemainingDays, isOverdue, suggestedSavingsPerDay,
        suggestedSavingsPerWeek, suggestedSavingsPerMonth, savings)
}
