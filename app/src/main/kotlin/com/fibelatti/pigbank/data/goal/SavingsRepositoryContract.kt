package com.fibelatti.pigbank.data.goal

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import io.reactivex.Single

@Dao
interface SavingsRepositoryContract {
    @Query(
        "select * from " + Savings.TABLE_NAME +
    " where " + Savings.COLUMN_GOAL_ID + " = :goalId"
    )
    fun getSavingsByGoalId(goalId: Long): Single<List<Savings>>

    @Query(
        "delete from " + Savings.TABLE_NAME +
            " where " + Savings.COLUMN_GOAL_ID + " = :goalId")
    fun deleteSavingsByGoalId(goalId: Long)

    @Query(
        "delete from " + Savings.TABLE_NAME +
            " where " + Savings.COLUMN_GOAL_ID + " in(:savingsIds)")
    fun deleteSavingsById(savingsIds: List<Long>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveSavings(vararg savings: Savings)
}
