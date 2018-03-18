package com.fibelatti.pigbank.presentation.models

import com.fibelatti.pigbank.domain.goal.models.GoalEntity
import javax.inject.Inject

class GoalPresentationMapper @Inject constructor(private val savingsPresentationMapper: SavingsPresentationMapper) {
    fun toDomainModel(goalPresentationModel: GoalPresentationModel): GoalEntity = with(goalPresentationModel) {
        GoalEntity(
            description,
            cost,
            deadline,
            id,
            creationDate,
            totalSaved,
            remainingCost,
            percentSaved,
            isAchieved,
            daysUntilDeadline,
            emphasizeRemainingDays,
            isOverdue,
            suggestedSavingsPerDay,
            suggestedSavingsPerWeek,
            suggestedSavingsPerMonth,
            savings.map { savingsPresentationMapper.toDomainModel(it) })
    }

    fun toPresentationModel(goalEntity: GoalEntity): GoalPresentationModel = with(goalEntity) {
        GoalPresentationModel(
            description,
            cost,
            deadline,
            id,
            creationDate,
            totalSaved,
            remainingCost,
            percentSaved,
            isAchieved,
            daysUntilDeadline,
            emphasizeRemainingDays,
            isOverdue,
            suggestedSavingsPerDay,
            suggestedSavingsPerWeek,
            suggestedSavingsPerMonth,
            savings.map { savingsPresentationMapper.toPresentationModel(it) }
        )
    }
}
