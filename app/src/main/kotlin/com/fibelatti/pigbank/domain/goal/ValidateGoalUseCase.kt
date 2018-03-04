package com.fibelatti.pigbank.domain.goal

import com.fibelatti.pigbank.common.isDate
import com.fibelatti.pigbank.common.isFloat
import com.fibelatti.pigbank.common.stringAsDate
import com.fibelatti.pigbank.domain.goal.InvalidGoalField.COST
import com.fibelatti.pigbank.domain.goal.InvalidGoalField.DEADLINE
import com.fibelatti.pigbank.domain.goal.InvalidGoalField.DESCRIPTION
import com.fibelatti.pigbank.domain.goal.InvalidGoalField.PAST_DEADLINE
import com.fibelatti.pigbank.presentation.models.Goal
import com.fibelatti.pigbank.presentation.models.GoalCandidate
import io.reactivex.Single
import java.util.Date
import javax.inject.Inject

enum class InvalidGoalField {
    DESCRIPTION, COST, DEADLINE, PAST_DEADLINE
}

class GoalValidationError(val field: InvalidGoalField) : Throwable()

class ValidateGoalUseCase @Inject constructor() {
    fun validateGoal(goalCandidate: GoalCandidate, now: Date): Single<Goal> = Single.create {
        when {
            goalCandidate.description.isBlank() -> it.onError(GoalValidationError(DESCRIPTION))
            goalCandidate.cost.isBlank() || !goalCandidate.cost.isFloat() -> it.onError(GoalValidationError(COST))
            goalCandidate.deadline.isBlank() || !goalCandidate.deadline.isDate() -> it.onError(GoalValidationError(DEADLINE))
            stringAsDate(goalCandidate.deadline).time - now.time < 0 -> it.onError(GoalValidationError(PAST_DEADLINE))
            else -> {
                it.onSuccess(Goal(
                    description = goalCandidate.description,
                    cost = goalCandidate.cost.toFloat(),
                    deadline = stringAsDate(goalCandidate.deadline)))
            }
        }
    }

    fun validateGoal(originalGoal: Goal, goalCandidate: GoalCandidate, now: Date): Single<Goal> = Single.create {
        when {
            goalCandidate.description.isBlank() -> it.onError(GoalValidationError(DESCRIPTION))
            goalCandidate.cost.isBlank() || !goalCandidate.cost.isFloat() -> it.onError(GoalValidationError(COST))
            goalCandidate.deadline.isBlank() || !goalCandidate.deadline.isDate() -> it.onError(GoalValidationError(DEADLINE))
            stringAsDate(goalCandidate.deadline).time - now.time < 0 -> it.onError(GoalValidationError(PAST_DEADLINE))
            else -> {
                it.onSuccess(originalGoal.deepCopy(
                    description = goalCandidate.description,
                    cost = goalCandidate.cost.toFloat(),
                    deadline = stringAsDate(goalCandidate.deadline)))
            }
        }
    }
}
