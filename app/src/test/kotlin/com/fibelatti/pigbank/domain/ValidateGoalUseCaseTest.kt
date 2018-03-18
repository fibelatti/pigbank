package com.fibelatti.pigbank.domain

import com.fibelatti.pigbank.BaseTest
import com.fibelatti.pigbank.any
import com.fibelatti.pigbank.domain.goal.GoalValidationError
import com.fibelatti.pigbank.domain.goal.InvalidGoalField.COST
import com.fibelatti.pigbank.domain.goal.InvalidGoalField.DEADLINE
import com.fibelatti.pigbank.domain.goal.InvalidGoalField.DESCRIPTION
import com.fibelatti.pigbank.domain.goal.InvalidGoalField.INVALID_DEADLINE
import com.fibelatti.pigbank.domain.goal.InvalidGoalField.PAST_DEADLINE
import com.fibelatti.pigbank.domain.goal.ValidateGoalUseCase
import com.fibelatti.pigbank.domain.goal.models.GoalCandidateEntity
import com.fibelatti.pigbank.domain.goal.models.GoalDomainMapper
import com.fibelatti.pigbank.domain.goal.models.GoalEntity
import io.reactivex.observers.TestObserver
import junit.framework.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito.mock
import java.util.Date

class ValidateGoalUseCaseTest : BaseTest() {

    //region Mocked Data
    private val mockNow = Date(1534723200000) //20/08/2018
    private val mockOriginalGoal = GoalEntity(description = "Before any changes", cost = 1000F, deadline = mockNow)

    private val invalidDescriptionEmpty = ""
    private val validDescription = "Test Description"

    private val invalidCostEmpty = ""
    private val invalidCostOther = "F"
    private val validCost = "1337"

    private val invalidDateEmpty = ""
    private val invalidDateOther = "30/02/2018"
    private val invalidDatePast = "20/08/2017"
    private val validDate = "20/08/2020"
    //endregion

    private val mockGoal: GoalEntity = mock(GoalEntity::class.java)

    private val mockGoalMapper: GoalDomainMapper = mock(GoalDomainMapper::class.java)
    private val validateGoalUseCase = ValidateGoalUseCase(mockGoalMapper)
    private val testObserver: TestObserver<GoalEntity> = TestObserver()

    @Before
    fun setup() {
        given(mockGoalMapper.toDomainModel(any<GoalCandidateEntity>()))
            .willReturn(mockGoal)
    }

    @Test
    fun testValidateGoalEmptyDescriptionReturnsValidationErrorDescriptionField() {
        // Arrange
        val goalCandidate = GoalCandidateEntity(invalidDescriptionEmpty, invalidCostEmpty, invalidDateEmpty)

        // Act
        validateGoalUseCase.validateGoal(goalCandidate, mockNow)
            .subscribe(testObserver)

        // Assert
        testObserver.assertNoValues()
        testObserver.assertError(GoalValidationError::class.java)
        (testObserver.errors()[0] as? GoalValidationError)?.let {
            Assert.assertEquals(DESCRIPTION, it.field)
        }
    }

    @Test
    fun testValidateGoalEmptyCostReturnsValidationErrorCostField() {
        // Arrange
        val goalCandidate = GoalCandidateEntity(validDescription, invalidCostEmpty, invalidDateEmpty)

        // Act
        validateGoalUseCase.validateGoal(goalCandidate, mockNow)
            .subscribe(testObserver)

        // Assert
        testObserver.assertNoValues()
        testObserver.assertError(GoalValidationError::class.java)
        (testObserver.errors()[0] as? GoalValidationError)?.let {
            Assert.assertEquals(COST, it.field)
        }
    }

    @Test
    fun testValidateGoalInvalidCostReturnsValidationErrorCostField() {
        // Arrange
        val goalCandidate = GoalCandidateEntity(validDescription, invalidCostOther, invalidDateEmpty)

        // Act
        validateGoalUseCase.validateGoal(goalCandidate, mockNow)
            .subscribe(testObserver)

        // Assert
        testObserver.assertNoValues()
        testObserver.assertError(GoalValidationError::class.java)
        (testObserver.errors()[0] as? GoalValidationError)?.let {
            Assert.assertEquals(COST, it.field)
        }
    }

    @Test
    fun testValidateGoalEmptyDateReturnsValidationErrorDeadlineField() {
        // Arrange
        val goalCandidate = GoalCandidateEntity(validDescription, validCost, invalidDateEmpty)

        // Act
        validateGoalUseCase.validateGoal(goalCandidate, mockNow)
            .subscribe(testObserver)

        // Assert
        testObserver.assertNoValues()
        testObserver.assertError(GoalValidationError::class.java)
        (testObserver.errors()[0] as? GoalValidationError)?.let {
            Assert.assertEquals(DEADLINE, it.field)
        }
    }

    @Test
    fun testValidateGoalInvalidDateReturnsValidationErrorInvalidDeadlineField() {
        // Arrange
        val goalCandidate = GoalCandidateEntity(validDescription, validCost, invalidDateOther)

        // Act
        validateGoalUseCase.validateGoal(goalCandidate, mockNow)
            .subscribe(testObserver)

        // Assert
        testObserver.assertNoValues()
        testObserver.assertError(GoalValidationError::class.java)
        (testObserver.errors()[0] as? GoalValidationError)?.let {
            Assert.assertEquals(INVALID_DEADLINE, it.field)
        }
    }

    @Test
    fun testValidateGoalPastDateReturnsValidationErrorPastDeadlineField() {
        // Arrange
        val goalCandidate = GoalCandidateEntity(validDescription, validCost, invalidDatePast)

        // Act
        validateGoalUseCase.validateGoal(goalCandidate, mockNow)
            .subscribe(testObserver)

        // Assert
        testObserver.assertNoValues()
        testObserver.assertError(GoalValidationError::class.java)
        (testObserver.errors()[0] as? GoalValidationError)?.let {
            Assert.assertEquals(PAST_DEADLINE, it.field)
        }
    }

    @Test
    fun testValidateGoalAllFieldsAreValidReturnsGoal() {
        // Arrange
        val goalCandidate = GoalCandidateEntity(validDescription, validCost, validDate)

        // Act
        validateGoalUseCase.validateGoal(goalCandidate, mockNow)
            .subscribe(testObserver)

        // Assert
        testObserver.assertNoErrors()
        testObserver.assertValueCount(1)
    }

    @Test
    fun testValidateGoalAllFieldsAreValidReturnsUpdatedGoal() {
        // Arrange
        val goalCandidate = GoalCandidateEntity(validDescription, validCost, validDate)

        // Act
        validateGoalUseCase.validateGoal(mockOriginalGoal, goalCandidate, mockNow)
            .subscribe(testObserver)

        // Assert
        testObserver.assertNoErrors()
        testObserver.assertValueCount(1)
    }
}
