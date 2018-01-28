package com.fibelatti.pigbank.data.goal

import android.support.test.runner.AndroidJUnit4
import com.fibelatti.pigbank.data.BaseDbTest
import io.reactivex.observers.TestObserver
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Date
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@RunWith(AndroidJUnit4::class)
class GoalRepositoryLocalDataSourceTest : BaseDbTest() {

    //region Mocked Data
    private val goalId = 1L
    private val goalDescription = "New MacBook Pro"
    private val goalCost = 2000F
    private val goalSavingsBefore = 0F
    private val goalSavingsAfter = 300F
    private val goalDeadline = Date(1546214400) // 2018/12/31

    private val firstSavingsId = 10L
    private val firstSavingsAmount = 300F
    private val firstSavingsDate = Date(1517356800) // 2018/01/31

    private val secondSavingsId = 20L
    private val secondSavingsAmount = 200F
    private val secondSavingsDate = Date(1517356800) // 2018/01/31
    //endregion

    @Test
    fun updateGoal() {
        // Arrange
        val firstTestObserver = TestObserver<GoalWithSavings>()

        insertGoal(goalSavingsAfter)
        insertSavings()

        appDatabase.getGoalRepository()
            .getGoalById(goalId)
            .subscribeOn(testSchedulerProvider.io())
            .observeOn(testSchedulerProvider.mainThread())
            .subscribe(firstTestObserver)

        assertSingleOnCompleteWithNoErrors(firstTestObserver)
        assertGoal(firstTestObserver, goalSavingsAfter)
        assertSavings(firstTestObserver)

        // Act
        val savings = arrayOf(
            Savings(firstSavingsId, goalId, firstSavingsAmount, firstSavingsDate),
            Savings(secondSavingsId, goalId, secondSavingsAmount, secondSavingsDate)
        )

        appDatabase.getSavingsRepository().saveSavings(*savings)

        // Assert
        val secondTestObserver = TestObserver<GoalWithSavings>()
        appDatabase.getGoalRepository()
            .getGoalById(goalId)
            .subscribeOn(testSchedulerProvider.io())
            .observeOn(testSchedulerProvider.mainThread())
            .subscribe(secondTestObserver)

        assertSingleOnCompleteWithNoErrors(secondTestObserver)
        val result = secondTestObserver.values()[0]
        with(result) {
            assertEquals(goalId, goal.id)
            assertEquals(goalDescription, goal.description)
            assertEquals(goalCost, goal.cost)
            assertEquals(goalSavingsAfter, goal.savings)
            assertEquals(goalDeadline, goal.deadline)

            assertEquals(2, result.savings.size)

            assertEquals(firstSavingsId, result.savings[0].id)
            assertEquals(firstSavingsAmount, result.savings[0].amount)
            assertEquals(firstSavingsDate, result.savings[0].date)

            assertEquals(secondSavingsId, result.savings[1].id)
            assertEquals(secondSavingsAmount, result.savings[1].amount)
            assertEquals(secondSavingsDate, result.savings[1].date)
        }
    }

    @Test
    fun getGroupById() {
        // Arrange
        val testObserver = TestObserver<GoalWithSavings>()
        insertGoal()

        // Act
        appDatabase.getGoalRepository()
            .getGoalById(goalId)
            .subscribeOn(testSchedulerProvider.io())
            .observeOn(testSchedulerProvider.mainThread())
            .subscribe(testObserver)

        // Assert
        assertSingleOnCompleteWithNoErrors(testObserver)
        assertGoal(testObserver)
    }

    @Test
    fun getGoalByIdWithItems() {
        // Arrange
        val testObserver = TestObserver<GoalWithSavings>()
        insertGoal(goalSavingsAfter)
        insertSavings()

        // Act
        appDatabase.getGoalRepository()
            .getGoalById(goalId)
            .subscribeOn(testSchedulerProvider.io())
            .observeOn(testSchedulerProvider.mainThread())
            .subscribe(testObserver)

        // Assert
        assertSingleOnCompleteWithNoErrors(testObserver)
        assertGoal(testObserver, goalSavingsAfter)
        assertSavings(testObserver)
    }

    @Test
    fun deleteGoalById() {
        // Arrange
        val testObserver = TestObserver<List<Goal>>()
        insertGoal(goalSavingsAfter)
        insertSavings()

        // Act
        appDatabase.getGoalRepository().deleteGoalById(goalId)

        // Assert
        appDatabase.getGoalRepository()
            .getAllGoals()
            .subscribeOn(testSchedulerProvider.io())
            .observeOn(testSchedulerProvider.mainThread())
            .subscribe(testObserver)

        assertSingleOnCompleteWithNoErrors(testObserver)
        assertTrue(testObserver.values()[0].isEmpty())
    }

    @Test
    fun getAllGoals() {
        // Arrange
        val testObserver = TestObserver<List<Goal>>()
        insertGoal()

        // Act
        appDatabase.getGoalRepository()
            .getAllGoals()
            .subscribeOn(testSchedulerProvider.io())
            .observeOn(testSchedulerProvider.mainThread())
            .subscribe(testObserver)

        // Assert
        assertSingleOnCompleteWithNoErrors(testObserver)
        assertTrue(testObserver.values()[0].isNotEmpty())
    }

    private fun createGoal(savings: Float = goalSavingsBefore) = Goal(goalId, goalDescription, goalCost, savings, goalDeadline)

    private fun createSavings() = Savings(firstSavingsId, goalId, firstSavingsAmount, firstSavingsDate)

    private fun insertGoal(savings: Float = goalSavingsBefore) {
        appDatabase.getGoalRepository().saveGoal(createGoal(savings))
    }

    private fun insertSavings() {
        appDatabase.getSavingsRepository().saveSavings(createSavings())
    }

    private fun assertGoal(testObserver: TestObserver<GoalWithSavings>, goalSavings: Float = goalSavingsBefore) {
        val result = testObserver.values()[0]
        with(result) {
            assertEquals(goalId, goal.id)
            assertEquals(goalDescription, goal.description)
            assertEquals(goalCost, goal.cost)
            assertEquals(goalSavings, goal.savings)
            assertEquals(goalDeadline, goal.deadline)
        }
    }

    private fun assertSavings(testObserver: TestObserver<GoalWithSavings>) {
        val result = testObserver.values()[0]

        assertTrue(result.savings.isNotEmpty())
        with(result.savings[0]) {
            assertEquals(firstSavingsId, id)
            assertEquals(firstSavingsAmount, amount)
            assertEquals(firstSavingsDate, date)
        }
    }
}
