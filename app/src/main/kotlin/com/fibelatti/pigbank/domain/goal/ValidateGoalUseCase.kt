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

class ValidateGoalUseCase @Inject constructor(private val goalMapper: GoalMapper) {
    fun validateGoal(goalCandidate: GoalCandidate, now: Date): Single<Goal> = Single.create { emitter ->
        with(goalCandidate) {
            when {
                description.isBlank() -> emitter.onError(GoalValidationError(DESCRIPTION))
                cost.isBlank() || !cost.isFloat() -> emitter.onError(GoalValidationError(COST))
                deadline.isBlank() || !deadline.isDate() -> emitter.onError(GoalValidationError(DEADLINE))
                stringAsDate(deadline).time - now.time < 0 -> emitter.onError(GoalValidationError(PAST_DEADLINE))
                else -> {
                    emitter.onSuccess(goalMapper.toPresentationModel(goalCandidate = this))
                }
            }
        }
    }

    fun validateGoal(originalGoal: Goal, goalCandidate: GoalCandidate, now: Date): Single<Goal> = Single.create { emitter ->
        with(goalCandidate) {
            when {
                description.isBlank() -> emitter.onError(GoalValidationError(DESCRIPTION))
                cost.isBlank() || !cost.isFloat() -> emitter.onError(GoalValidationError(COST))
                deadline.isBlank() || !deadline.isDate() -> emitter.onError(GoalValidationError(DEADLINE))
                stringAsDate(deadline).time - now.time < 0 -> emitter.onError(GoalValidationError(PAST_DEADLINE))
                else -> emitter.onSuccess(originalGoal.deepCopy(
                    description = description,
                    cost = cost.toFloat(),
                    deadline = stringAsDate(deadline)))
            }
        }
    }
}
