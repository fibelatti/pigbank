package com.fibelatti.pigbank.domain.goal

import com.fibelatti.pigbank.common.isFloat
import com.fibelatti.pigbank.domain.goal.InvalidSavingsField.AMOUNT
import io.reactivex.Observable
import javax.inject.Inject

enum class InvalidSavingsField {
    AMOUNT
}

class SavingsValidationError(val field: InvalidSavingsField) : Throwable()

class ValidateSavingsUseCase @Inject constructor() {
    fun validateSavings(amount: String): Observable<Float> = when {
        amount.isBlank() || !amount.isFloat() -> Observable.error(SavingsValidationError(AMOUNT))
        else -> Observable.just(amount.toFloat())
    }
}
