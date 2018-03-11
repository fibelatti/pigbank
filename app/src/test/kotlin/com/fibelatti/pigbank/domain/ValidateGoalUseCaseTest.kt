package com.fibelatti.pigbank.domain

import com.fibelatti.pigbank.BaseTest
import com.fibelatti.pigbank.any
import com.fibelatti.pigbank.domain.goal.GoalMapper
import com.fibelatti.pigbank.domain.goal.GoalValidationError
import com.fibelatti.pigbank.domain.goal.InvalidGoalField.COST
import com.fibelatti.pigbank.domain.goal.InvalidGoalField.DEADLINE
import com.fibelatti.pigbank.domain.goal.InvalidGoalField.DESCRIPTION
import com.fibelatti.pigbank.domain.goal.InvalidGoalField.PAST_DEADLINE
import com.fibelatti.pigbank.domain.goal.ValidateGoalUseCase
import com.fibelatti.pigbank.presentation.models.Goal
import com.fibelatti.pigbank.presentation.models.GoalCandidate
import io.reactivex.observers.TestObserver
import junit.framework.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito.mock
import java.util.Date

class ValidateGoalUseCaseTest : BaseTest() {

    //region Mocked Data
    private val mockedNow = Date(1534723200000) //20/08/2018
    private val mockedGoal = Goal(description = "Before any changes", cost = 1000F, deadline = mockedNow)

    private val invalidDescriptionEmpty = ""
    private val validDescription = "Test Description"

    private val invalidCostEmpty = ""
    private val invalidCostOther = "F"
    private val validCost = "1337"

    private val invalidDateEmpty = ""
    private val invalidDateOther = "F"
    private val invalidDatePast = "20/08/2017"
    private val validDate = "20/08/2020"
    //endregion

    private val mockGoal: Goal = mock(Goal::class.java)

    private val mockGoalMapper: GoalMapper = mock(GoalMapper::class.java)
    private val validateGoalUseCase = ValidateGoalUseCase(mockGoalMapper)
    private var testObserver: TestObserver<Goal> = TestObserver()

    @Before
    fun setup() {
        given(mockGoalMapper.toPresentationModel(any<GoalCandidate>()))
            .willReturn(mockGoal)
    }

    @Test
    fun testValidateGoalEmptyDescriptionReturnsValidationErrorDescriptionField() {
        // Arrange
        val goalCandidate = GoalCandidate(invalidDescriptionEmpty, invalidCostEmpty, invalidDateEmpty)

        // Act
        validateGoalUseCase.validateGoal(goalCandidate, mockedNow)
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
        val goalCandidate = GoalCandidate(validDescription, invalidCostEmpty, invalidDateEmpty)

        // Act
        validateGoalUseCase.validateGoal(goalCandidate, mockedNow)
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
        val goalCandidate = GoalCandidate(validDescription, invalidCostOther, invalidDateEmpty)

        // Act
        validateGoalUseCase.validateGoal(goalCandidate, mockedNow)
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
        val goalCandidate = GoalCandidate(validDescription, validCost, invalidDateEmpty)

        // Act
        validateGoalUseCase.validateGoal(goalCandidate, mockedNow)
            .subscribe(testObserver)

        // Assert
        testObserver.assertNoValues()
        testObserver.assertError(GoalValidationError::class.java)
        (testObserver.errors()[0] as? GoalValidationError)?.let {
            Assert.assertEquals(DEADLINE, it.field)
        }
    }

    @Test
    fun testValidateGoalInvalidDateReturnsValidationErrorDeadlineField() {
        // Arrange
        val goalCandidate = GoalCandidate(validDescription, validCost, invalidDateOther)

        // Act
        validateGoalUseCase.validateGoal(goalCandidate, mockedNow)
            .subscribe(testObserver)

        // Assert
        testObserver.assertNoValues()
        testObserver.assertError(GoalValidationError::class.java)
        (testObserver.errors()[0] as? GoalValidationError)?.let {
            Assert.assertEquals(DEADLINE, it.field)
        }
    }

    @Test
    fun testValidateGoalPastDateReturnsValidationErrorPastDeadlineField() {
        // Arrange
        val goalCandidate = GoalCandidate(validDescription, validCost, invalidDatePast)

        // Act
        validateGoalUseCase.validateGoal(goalCandidate, mockedNow)
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
        val goalCandidate = GoalCandidate(validDescription, validCost, validDate)

        // Act
        validateGoalUseCase.validateGoal(goalCandidate, mockedNow)
            .subscribe(testObserver)

        // Assert
        testObserver.assertNoErrors()
        testObserver.assertValueCount(1)
    }

    @Test
    fun testValidateGoalAllFieldsAreValidReturnsUpdatedGoal() {
        // Arrange
        val goalCandidate = GoalCandidate(validDescription, validCost, validDate)

        // Act
        validateGoalUseCase.validateGoal(mockedGoal, goalCandidate, mockedNow)
            .subscribe(testObserver)

        // Assert
        testObserver.assertNoErrors()
        testObserver.assertValueCount(1)
    }
}
