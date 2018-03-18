package com.fibelatti.pigbank.domain.goal

import com.fibelatti.pigbank.common.isFloat
import com.fibelatti.pigbank.common.toNormalizedFloat
import com.fibelatti.pigbank.domain.goal.InvalidSavingsField.AMOUNT
import io.reactivex.Single
import javax.inject.Inject

enum class InvalidSavingsField {
    AMOUNT
}

class SavingsValidationError(val field: InvalidSavingsField) : Throwable()

class ValidateSavingsUseCase @Inject constructor() {
    fun validateSavings(amount: String): Single<Float> = Single.create {
        when {
            amount.isBlank() || !amount.isFloat() -> it.onError(SavingsValidationError(AMOUNT))
            else -> it.onSuccess(amount.toNormalizedFloat())
        }
    }
}
