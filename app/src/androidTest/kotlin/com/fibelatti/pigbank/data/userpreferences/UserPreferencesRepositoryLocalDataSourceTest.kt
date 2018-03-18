package com.fibelatti.pigbank.data.userpreferences

import android.support.test.runner.AndroidJUnit4
import com.fibelatti.pigbank.data.BaseDbTest
import io.reactivex.observers.TestObserver
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserPreferencesRepositoryLocalDataSourceTest : BaseDbTest() {

    @Test
    fun getAllSettings() {
        // Arrange
        val testObserver = TestObserver<List<UserPreferencesDataModel>>()

        // Act
        appDatabase.getUserPreferencesRepository()
            .getAllUserPreferences()
            .subscribeOn(testSchedulerProvider.io())
            .observeOn(testSchedulerProvider.mainThread())
            .subscribe(testObserver)

        // Assert
        val result = testObserver.values().firstOrNull()
        assertSingleOnCompleteWithNoErrors(testObserver)
        assertTrue(result != null)
        result?.let {
            assertTrue(it.size == 4)
        }
    }

    @Test
    fun getAllToggles() {
        // Arrange
        val testObserver = TestObserver<List<UserPreferencesDataModel>>()

        // Act
        appDatabase.getUserPreferencesRepository()
            .getAllByType(UserPreferencesType.TOGGLE.value)
            .subscribeOn(testSchedulerProvider.io())
            .observeOn(testSchedulerProvider.mainThread())
            .subscribe(testObserver)

        // Assert
        val result = testObserver.values().firstOrNull()
        assertSingleOnCompleteWithNoErrors(testObserver)
        assertTrue(result != null)
        result?.let {
            assertTrue(it.size == 2)
        }
    }

    @Test
    fun updateAllByType() {
        // Arrange
        val testObserver = TestObserver<List<UserPreferencesDataModel>>()

        appDatabase.getUserPreferencesRepository()
            .updateAllByType(UserPreferencesType.TOGGLE.value, false.toString())

        // Act
        appDatabase.getUserPreferencesRepository()
            .getAllByType(UserPreferencesType.TOGGLE.value)
            .subscribeOn(testSchedulerProvider.io())
            .observeOn(testSchedulerProvider.mainThread())
            .subscribe(testObserver)

        // Assert
        val result = testObserver.values().firstOrNull()
        assertSingleOnCompleteWithNoErrors(testObserver)
        assertTrue(result != null)
        result?.let {
            assertTrue(it.size == 2)
            it.forEach {
                assertEquals(false.toString(), it.value)
            }
        }
    }

    @Test
    fun updateAnalyticsEnabled() {
        // Arrange
        val testObserver = TestObserver<List<UserPreferencesDataModel>>()

        appDatabase.getUserPreferencesRepository()
            .updateAnalyticsEnabled(false.toString())

        // Act
        appDatabase.getUserPreferencesRepository()
            .getAllByType(UserPreferencesType.TOGGLE.value)
            .subscribeOn(testSchedulerProvider.io())
            .observeOn(testSchedulerProvider.mainThread())
            .subscribe(testObserver)

        // Assert
        val result = testObserver.values().firstOrNull()
        val updatedValues = result?.filter { it.name == USER_PREFERENCE_NAME_ANALYTICS_ENABLED }

        assertSingleOnCompleteWithNoErrors(testObserver)
        assertTrue(result != null)
        assertTrue(updatedValues != null)
        updatedValues?.let {
            assertTrue(it.isNotEmpty())
            assertEquals(false.toString(), it.first().value)
        }
    }

    @Test
    fun updateCrashReportsEnabled() {
        // Arrange
        val testObserver = TestObserver<List<UserPreferencesDataModel>>()

        appDatabase.getUserPreferencesRepository()
            .updateCrashReportsEnabled(false.toString())

        // Act
        appDatabase.getUserPreferencesRepository()
            .getAllByType(UserPreferencesType.TOGGLE.value)
            .subscribeOn(testSchedulerProvider.io())
            .observeOn(testSchedulerProvider.mainThread())
            .subscribe(testObserver)

        // Assert
        val result = testObserver.values().firstOrNull()
        val updatedValues = result?.filter { it.name == USER_PREFERENCE_NAME_CRASH_REPORTS_ENABLED }

        assertSingleOnCompleteWithNoErrors(testObserver)
        assertTrue(result != null)
        assertTrue(updatedValues != null)
        updatedValues?.let {
            assertTrue(it.isNotEmpty())
            assertEquals(false.toString(), it.first().value)
        }
    }

    @Test
    fun setFirstGoalHintDismissed() {
        // Arrange
        val testObserver = TestObserver<List<UserPreferencesDataModel>>()

        appDatabase.getUserPreferencesRepository()
            .setFirstGoalHintDismissed()

        // Act
        appDatabase.getUserPreferencesRepository()
            .getAllByType(UserPreferencesType.HINT.value)
            .subscribeOn(testSchedulerProvider.io())
            .observeOn(testSchedulerProvider.mainThread())
            .subscribe(testObserver)

        // Assert
        val result = testObserver.values().firstOrNull()
        val updatedValues = result?.filter { it.name == USER_PREFERENCE_NAME_FIRST_GOAL_HINT_DISMISSED }

        assertSingleOnCompleteWithNoErrors(testObserver)
        assertTrue(result != null)
        assertTrue(updatedValues != null)
        updatedValues?.let {
            assertTrue(it.isNotEmpty())
            assertEquals(true.toString(), it.first().value)
        }
    }

    @Test
    fun setQuickSaveHintDismissed() {
        // Arrange
        val testObserver = TestObserver<List<UserPreferencesDataModel>>()

        appDatabase.getUserPreferencesRepository()
            .setQuickSaveHintDismissed()

        // Act
        appDatabase.getUserPreferencesRepository()
            .getAllByType(UserPreferencesType.HINT.value)
            .subscribeOn(testSchedulerProvider.io())
            .observeOn(testSchedulerProvider.mainThread())
            .subscribe(testObserver)

        // Assert
        val result = testObserver.values().firstOrNull()
        val updatedValues = result?.filter { it.name == USER_PREFERENCE_NAME_QUICK_SAVE_HINT_DISMISSED }

        assertSingleOnCompleteWithNoErrors(testObserver)
        assertTrue(result != null)
        assertTrue(updatedValues != null)
        updatedValues?.let {
            assertTrue(it.isNotEmpty())
            assertEquals(true.toString(), it.first().value)
        }
    }
}
