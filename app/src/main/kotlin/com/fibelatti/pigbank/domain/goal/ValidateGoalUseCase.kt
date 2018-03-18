package com.fibelatti.pigbank.domain.goal

import com.fibelatti.pigbank.common.isDate
import com.fibelatti.pigbank.common.isFloat
import com.fibelatti.pigbank.common.isValidDate
import com.fibelatti.pigbank.common.stringAsDate
import com.fibelatti.pigbank.domain.goal.InvalidGoalField.COST
import com.fibelatti.pigbank.domain.goal.InvalidGoalField.DEADLINE
import com.fibelatti.pigbank.domain.goal.InvalidGoalField.DESCRIPTION
import com.fibelatti.pigbank.domain.goal.InvalidGoalField.INVALID_DEADLINE
import com.fibelatti.pigbank.domain.goal.InvalidGoalField.PAST_DEADLINE
import com.fibelatti.pigbank.domain.goal.models.GoalCandidateEntity
import com.fibelatti.pigbank.domain.goal.models.GoalDomainMapper
import com.fibelatti.pigbank.domain.goal.models.GoalEntity
import io.reactivex.Single
import java.util.Date
import javax.inject.Inject

enum class InvalidGoalField {
    DESCRIPTION, COST, DEADLINE, INVALID_DEADLINE, PAST_DEADLINE
}

class GoalValidationError(val field: InvalidGoalField) : Throwable()

class ValidateGoalUseCase @Inject constructor(private val goalDomainMapper: GoalDomainMapper) {
    fun validateGoal(goalCandidate: GoalCandidateEntity, now: Date): Single<GoalEntity> = Single.create { emitter ->
        with(goalCandidate) {
            when {
                description.isBlank() -> emitter.onError(GoalValidationError(DESCRIPTION))
                cost.isBlank() || !cost.isFloat() -> emitter.onError(GoalValidationError(COST))
                deadline.isBlank() -> emitter.onError(GoalValidationError(DEADLINE))
                !deadline.isDate() || !deadline.isValidDate() -> emitter.onError(GoalValidationError(INVALID_DEADLINE))
                stringAsDate(deadline).time - now.time < 0 -> emitter.onError(GoalValidationError(PAST_DEADLINE))
                else -> {
                    emitter.onSuccess(goalDomainMapper.toDomainModel(goalCandidate = this))
                }
            }
        }
    }

    fun validateGoal(originalGoal: GoalEntity, goalCandidate: GoalCandidateEntity, now: Date): Single<GoalEntity> = Single.create { emitter ->
        with(goalCandidate) {
            when {
                description.isBlank() -> emitter.onError(GoalValidationError(DESCRIPTION))
                cost.isBlank() || !cost.isFloat() -> emitter.onError(GoalValidationError(COST))
                deadline.isBlank() -> emitter.onError(GoalValidationError(DEADLINE))
                !deadline.isDate() || !deadline.isValidDate() -> emitter.onError(GoalValidationError(INVALID_DEADLINE))
                stringAsDate(deadline).time - now.time < 0 -> emitter.onError(GoalValidationError(PAST_DEADLINE))
                else -> emitter.onSuccess(originalGoal.deepCopy(
                    description = description,
                    cost = cost.toFloat(),
                    deadline = stringAsDate(deadline)))
            }
        }
    }
}
