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

@Database(
    entities = [Goal::class, Savings::class],
    version = DATABASE_CURRENT_VERSION,
    exportSchema = false)
@TypeConverters(AppTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getGoalRepository(): GoalRepositoryContract
    abstract fun getSavingsRepository(): SavingsRepositoryContract
}
