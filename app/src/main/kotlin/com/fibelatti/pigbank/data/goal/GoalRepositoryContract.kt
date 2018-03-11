package com.fibelatti.pigbank.data.goal

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import android.arch.persistence.room.Transaction
import com.fibelatti.pigbank.data.localdatasource.DELETE_FROM
import com.fibelatti.pigbank.data.localdatasource.SELECT_ALL_FROM
import com.fibelatti.pigbank.data.localdatasource.WHERE
import io.reactivex.Single

@Dao
interface GoalRepositoryContract {
    @Transaction
    @Query(value = "$SELECT_ALL_FROM ${Goal.TABLE_NAME} $WHERE ${Goal.COLUMN_ID} = :goalId")
    fun getGoalById(goalId: Long): Single<GoalWithSavings>

    @Query(value = "$SELECT_ALL_FROM ${Goal.TABLE_NAME}")
    fun getAllGoals(): Single<List<GoalWithSavings>>

    @Insert(onConflict = REPLACE)
    fun saveGoal(goal: Goal): Long

    @Query(value = "$DELETE_FROM ${Goal.TABLE_NAME} $WHERE ${Goal.COLUMN_ID} = :goalId")
    fun deleteGoalById(goalId: Long): Int
}
