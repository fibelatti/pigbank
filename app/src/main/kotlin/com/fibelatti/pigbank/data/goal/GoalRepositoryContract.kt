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
    @Query(value = "$SELECT_ALL_FROM ${GoalDataModel.TABLE_NAME} $WHERE ${GoalDataModel.COLUMN_ID} = :goalId")
    fun getGoalById(goalId: Long): Single<GoalWithSavingsDataModel>

    @Query(value = "$SELECT_ALL_FROM ${GoalDataModel.TABLE_NAME}")
    fun getAllGoals(): Single<List<GoalWithSavingsDataModel>>

    @Insert(onConflict = REPLACE)
    fun saveGoal(goalDataModel: GoalDataModel): Long

    @Query(value = "$DELETE_FROM ${GoalDataModel.TABLE_NAME} $WHERE ${GoalDataModel.COLUMN_ID} = :goalId")
    fun deleteGoalById(goalId: Long): Int
}
