package com.fibelatti.pigbank.data.localdatasource

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.fibelatti.pigbank.data.goal.Goal
import com.fibelatti.pigbank.data.goal.GoalRepositoryContract
import com.fibelatti.pigbank.data.goal.Savings
import com.fibelatti.pigbank.data.goal.SavingsRepositoryContract

const val DATABASE_NAME = "com.fibelatti.pigbank.data.db"

const val DATABASE_CURRENT_VERSION = 1

const val DATABASE_GENERIC_ERROR_MESSAGE = "An error occurred when trying to perform a database operation"

//region Common SQL Commands
const val SELECT_ALL_FROM = "select * from"
const val DELETE_FROM = "delete from"
const val UPDATE = "update"
const val INSERT_OR_REPLACE_INTO = "insert or replace into"
const val VALUES = "values"
const val SET = "set"
const val WHERE = "where"
//endregion
@Database(
    entities = [Goal::class, Savings::class],
    version = DATABASE_CURRENT_VERSION,
    exportSchema = false)
@TypeConverters(AppTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getGoalRepository(): GoalRepositoryContract
    abstract fun getSavingsRepository(): SavingsRepositoryContract
}
