package com.fibelatti.pigbank.domain.goal.models

import java.util.Date

data class GoalEntity(
    val description: String,
    val cost: Float,
    val deadline: Date,
    val id: Long = 0,
    val creationDate: Date = Date(),
    val savings: List<SavingsEntity> = emptyList(),
    val totalSaved: Float = 0F,
    val remainingCost: Float = 0F,
    val percentSaved: Float = 0F,
    val isAchieved: Boolean = false,
    val daysUntilDeadline: Int = 0,
    val isOverdue: Boolean = false,
    val timeElapsed: Int = 0
) {
    fun deepCopy(
        description: String = this.description,
        cost: Float = this.cost,
        deadline: Date = this.deadline,
        id: Long = this.id,
        creationDate: Date = this.creationDate,
        savings: List<SavingsEntity> = this.savings.map { it.copy() },
        totalSaved: Float = this.totalSaved,
        remainingCost: Float = this.remainingCost,
        percentSaved: Float = this.percentSaved,
        isAchieved: Boolean = this.isOverdue,
        daysUntilDeadline: Int = this.daysUntilDeadline,
        isOverdue: Boolean = this.isOverdue,
        timeElapsed: Int = this.timeElapsed
    ) = GoalEntity(description, cost, deadline, id, creationDate, savings, totalSaved,
        remainingCost, percentSaved, isAchieved, daysUntilDeadline, isOverdue, timeElapsed)
}
