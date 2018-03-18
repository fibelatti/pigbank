package com.fibelatti.pigbank.data.goal

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.fibelatti.pigbank.data.localdatasource.DELETE_FROM
import com.fibelatti.pigbank.data.localdatasource.SELECT_ALL_FROM
import com.fibelatti.pigbank.data.localdatasource.WHERE
import io.reactivex.Single

@Dao
interface SavingsRepositoryContract {
    @Query(value = "$SELECT_ALL_FROM ${SavingsDataModel.TABLE_NAME} $WHERE ${SavingsDataModel.COLUMN_GOAL_ID} = :goalId")
    fun getSavingsByGoalId(goalId: Long): Single<List<SavingsDataModel>>

    @Query(value = "$DELETE_FROM ${SavingsDataModel.TABLE_NAME} $WHERE ${SavingsDataModel.COLUMN_GOAL_ID} = :goalId")
    fun deleteSavingsByGoalId(goalId: Long): Int

    @Query(value = "$DELETE_FROM ${SavingsDataModel.TABLE_NAME} $WHERE ${SavingsDataModel.COLUMN_GOAL_ID} in(:savingsIds)")
    fun deleteSavingsById(savingsIds: List<Long>): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveSavings(vararg savings: SavingsDataModel): List<Long>
}
