package com.fibelatti.pigbank.data.userpreferences

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.fibelatti.pigbank.data.localdatasource.SELECT_ALL_FROM
import com.fibelatti.pigbank.data.localdatasource.SET
import com.fibelatti.pigbank.data.localdatasource.UPDATE
import com.fibelatti.pigbank.data.localdatasource.WHERE
import io.reactivex.Single

@Dao
interface UserPreferencesRepositoryContract {

    @Query(value = "$SELECT_ALL_FROM ${UserPreferencesDataModel.TABLE_NAME}")
    fun getAllUserPreferences(): Single<List<UserPreferencesDataModel>>

    @Query(value = "$SELECT_ALL_FROM ${UserPreferencesDataModel.TABLE_NAME} " +
        "$WHERE ${UserPreferencesDataModel.COLUMN_TYPE} = :type")
    fun getAllByType(type: String): Single<List<UserPreferencesDataModel>>

    @Query(value = "$UPDATE ${UserPreferencesDataModel.TABLE_NAME} " +
        "$SET ${UserPreferencesDataModel.COLUMN_VALUE} = :value " +
        "$WHERE ${UserPreferencesDataModel.COLUMN_TYPE} = :type")
    fun updateAllByType(type: String, value: String): Int

    @Query(value = "$UPDATE ${UserPreferencesDataModel.TABLE_NAME} " +
        "$SET ${UserPreferencesDataModel.COLUMN_VALUE} = :value " +
        "$WHERE ${UserPreferencesDataModel.COLUMN_NAME} = '$USER_PREFERENCE_NAME_ANALYTICS_ENABLED'")
    fun updateAnalyticsEnabled(value: String): Int

    @Query(value = "$UPDATE ${UserPreferencesDataModel.TABLE_NAME} " +
        "$SET ${UserPreferencesDataModel.COLUMN_VALUE} = :value " +
        "$WHERE ${UserPreferencesDataModel.COLUMN_NAME} = '$USER_PREFERENCE_NAME_CRASH_REPORTS_ENABLED'")
    fun updateCrashReportsEnabled(value: String): Int

    @Query(value = "$UPDATE ${UserPreferencesDataModel.TABLE_NAME} " +
        "$SET ${UserPreferencesDataModel.COLUMN_VALUE} = 'true' " +
        "$WHERE ${UserPreferencesDataModel.COLUMN_NAME} = '$USER_PREFERENCE_NAME_FIRST_GOAL_HINT_DISMISSED'")
    fun setFirstGoalHintDismissed(): Int

    @Query(value = "$UPDATE ${UserPreferencesDataModel.TABLE_NAME} " +
        "$SET ${UserPreferencesDataModel.COLUMN_VALUE} = 'true' " +
        "$WHERE ${UserPreferencesDataModel.COLUMN_NAME} = '$USER_PREFERENCE_NAME_QUICK_SAVE_HINT_DISMISSED'")
    fun setQuickSaveHintDismissed(): Int
}
