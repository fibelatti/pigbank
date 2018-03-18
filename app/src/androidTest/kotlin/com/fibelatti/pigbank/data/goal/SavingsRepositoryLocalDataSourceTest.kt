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
class SavingsRepositoryLocalDataSourceTest : BaseDbTest() {
    //region Mocked Data
    private val firstGoalId = 1L
    private val firstGoalCreationDate = Date(1517184000)
    private val firstGoalDescription = "New MacBook Pro"
    private val firstGoalCost = 2000F
    private val firstGoalDeadline = Date(1546214400) // 2018/12/31

    private val secondGoalId = 2L
    private val secondGoalCreationDate = Date(1517184000)
    private val secondGoalDescription = "Trip to Japan"
    private val secondGoalCost = 1500F
    private val secondGoalDeadline = Date(1546214400) // 2018/12/31

    private val firstSavingsId = 10L
    private val firstSavingsAmount = 500F
    private val firstSavingsDate = Date(1517356800) // 2018/01/31

    private val secondSavingsId = 20L
    private val secondSavingsAmount = 200F
    private val secondSavingsDate = Date(1519862400) // 2018/03/01

    private val thirdSavingsId = 30L
    private val thirdSavingsAmount = 300F
    private val thirdSavingsDate = Date(1525132800) // 2018/05/01
    //endregion

    @Test
    fun getSavingsByGoalId() {
        // Arrange
        val testObserver = TestObserver<List<SavingsDataModel>>()
        createSampleData()

        // Act
        appDatabase.getSavingsRepository()
            .getSavingsByGoalId(firstGoalId)
            .subscribeOn(testSchedulerProvider.io())
            .observeOn(testSchedulerProvider.mainThread())
            .subscribe(testObserver)

        // Assert
        val result = testObserver.values()[0]

        assertSingleOnCompleteWithNoErrors(testObserver)
        assertTrue(result.isNotEmpty())
        assertEquals(1, result.size)
    }

    @Test
    fun deleteSavingsByGoalId() {
        // Arrange
        val testObserver = TestObserver<List<SavingsDataModel>>()
        val testObserverOther = TestObserver<List<SavingsDataModel>>()
        createSampleData()

        // Act
        appDatabase.getSavingsRepository()
            .deleteSavingsByGoalId(firstGoalId)

        // Assert
        appDatabase.getSavingsRepository()
            .getSavingsByGoalId(firstGoalId)
            .subscribeOn(testSchedulerProvider.io())
            .observeOn(testSchedulerProvider.mainThread())
            .subscribe(testObserver)
        val result = testObserver.values()[0]

        assertSingleOnCompleteWithNoErrors(testObserver)
        assertEquals(0, result.size)

        appDatabase.getSavingsRepository()
            .getSavingsByGoalId(secondGoalId)
            .subscribeOn(testSchedulerProvider.io())
            .observeOn(testSchedulerProvider.mainThread())
            .subscribe(testObserverOther)
        val resultOther = testObserverOther.values()[0]

        assertSingleOnCompleteWithNoErrors(testObserverOther)
        assertEquals(2, resultOther.size)
    }

    @Test
    fun deleteSavingsById() {
        // Arrange
        val testObserver = TestObserver<List<SavingsDataModel>>()
        createSampleData()

        // Act
        appDatabase.getSavingsRepository()
            .deleteSavingsById(listOf(secondSavingsId, thirdSavingsId))

        // Assert
        appDatabase.getSavingsRepository()
            .getSavingsByGoalId(secondSavingsId)
            .subscribeOn(testSchedulerProvider.io())
            .observeOn(testSchedulerProvider.mainThread())
            .subscribe(testObserver)
        val result = testObserver.values()[0]

        assertSingleOnCompleteWithNoErrors(testObserver)
        assertEquals(0, result.size)
    }

    private fun createSampleData() {
        insertGoal(GoalDataModel(firstGoalId, firstGoalCreationDate, firstGoalDescription, firstGoalCost, firstGoalDeadline))
        insertGoal(GoalDataModel(secondGoalId, secondGoalCreationDate, secondGoalDescription, secondGoalCost, secondGoalDeadline))
        insertSavings(SavingsDataModel(firstSavingsId, firstGoalId, firstSavingsAmount, firstSavingsDate))
        insertSavings(SavingsDataModel(secondSavingsId, secondGoalId, secondSavingsAmount, secondSavingsDate))
        insertSavings(SavingsDataModel(thirdSavingsId, secondGoalId, thirdSavingsAmount, thirdSavingsDate))
    }

    private fun insertGoal(goalDataModel: GoalDataModel) {
        appDatabase.getGoalRepository().saveGoal(goalDataModel)
    }

    private fun insertSavings(savingsDataModel: SavingsDataModel) {
        appDatabase.getSavingsRepository().saveSavings(savingsDataModel)
    }
}
