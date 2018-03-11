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

    @Query(value = "$SELECT_ALL_FROM ${UserPreferencesEntity.TABLE_NAME}")
    fun getAllUserPreferences(): Single<List<UserPreferencesEntity>>

    @Query(value = "$SELECT_ALL_FROM ${UserPreferencesEntity.TABLE_NAME} " +
        "$WHERE ${UserPreferencesEntity.COLUMN_TYPE} = :type")
    fun getAllByType(type: String): Single<List<UserPreferencesEntity>>

    @Query(value = "$UPDATE ${UserPreferencesEntity.TABLE_NAME} " +
        "$SET ${UserPreferencesEntity.COLUMN_VALUE} = :value " +
        "$WHERE ${UserPreferencesEntity.COLUMN_TYPE} = :type")
    fun updateAllByType(type: String, value: String): Int

    @Query(value = "$UPDATE ${UserPreferencesEntity.TABLE_NAME} " +
        "$SET ${UserPreferencesEntity.COLUMN_VALUE} = :value " +
        "$WHERE ${UserPreferencesEntity.COLUMN_NAME} = '$USER_PREFERENCE_NAME_ANALYTICS_ENABLED'")
    fun updateAnalyticsEnabled(value: String): Int

    @Query(value = "$UPDATE ${UserPreferencesEntity.TABLE_NAME} " +
        "$SET ${UserPreferencesEntity.COLUMN_VALUE} = :value " +
        "$WHERE ${UserPreferencesEntity.COLUMN_NAME} = '$USER_PREFERENCE_NAME_CRASH_REPORTS_ENABLED'")
    fun updateCrashReportsEnabled(value: String): Int
}
