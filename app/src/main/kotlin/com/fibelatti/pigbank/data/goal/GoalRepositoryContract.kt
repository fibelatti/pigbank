package com.fibelatti.pigbank.data.goal

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import android.arch.persistence.room.Transaction
import io.reactivex.Single

@Dao
interface GoalRepositoryContract {
    @Transaction
    @Query(
        "select * from " + Goal.TABLE_NAME +
            " where " + Goal.COLUMN_ID + " = :goalId"
    )
    fun getGoalById(goalId: Long): Single<GoalWithSavings>

    @Query("select * from " + Goal.TABLE_NAME)
    fun getAllGoals(): Single<List<Goal>>

    @Insert(onConflict = REPLACE)
    fun saveGoal(goal: Goal)

    @Query(
        "delete from " + Goal.TABLE_NAME +
            " where " + Goal.COLUMN_ID + " = :goalId"
    )
    fun deleteGoalById(goalId: Long)
}
