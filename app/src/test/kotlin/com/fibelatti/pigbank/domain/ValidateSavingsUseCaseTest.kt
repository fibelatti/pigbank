package com.fibelatti.pigbank.domain

import com.fibelatti.pigbank.BaseTest
import com.fibelatti.pigbank.domain.goal.InvalidSavingsField.AMOUNT
import com.fibelatti.pigbank.domain.goal.SavingsValidationError
import com.fibelatti.pigbank.domain.goal.ValidateSavingsUseCase
import io.reactivex.observers.TestObserver
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ValidateSavingsUseCaseTest : BaseTest() {

    //region Mocked Data
    private val expectedAmount = 33.50F
    private val validAmount = "33.50"
    private val invalidAmountEmpty = ""
    private val invalidAmountOther = "F"
    //endregion

    private val validateSavingsUseCase = ValidateSavingsUseCase()
    private lateinit var testObserver: TestObserver<Float>

    @Before
    fun setup() {
        testObserver = TestObserver()
    }

    @Test
    fun testValidateSavingsEmptyInputReturnsError() {
        // Act
        validateSavingsUseCase.validateSavings(invalidAmountEmpty)
            .subscribe(testObserver)

        // Assert
        testObserver.assertNoValues()
        testObserver.assertError(SavingsValidationError::class.java)
        (testObserver.errors()[0] as? SavingsValidationError)?.let {
            assertEquals(AMOUNT, it.field)
        }
    }

    @Test
    fun testValidateSavingsInvalidInputReturnsError() {
        // Act
        validateSavingsUseCase.validateSavings(invalidAmountOther)
            .subscribe(testObserver)

        // Assert
        testObserver.assertNoValues()
        testObserver.assertError(SavingsValidationError::class.java)
        (testObserver.errors()[0] as? SavingsValidationError)?.let {
            assertEquals(AMOUNT, it.field)
        }
    }

    @Test
    fun testValidateSavingsValidInputReturnsFloat() {
        // Act
        validateSavingsUseCase.validateSavings(validAmount)
            .subscribe(testObserver)

        // Assert
        testObserver.assertValueCount(1)
        testObserver.assertValue(expectedAmount)
    }
}
