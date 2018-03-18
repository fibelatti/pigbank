package com.fibelatti.pigbank.data.localdatasource

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.fibelatti.pigbank.data.goal.GoalDataModel
import com.fibelatti.pigbank.data.goal.GoalRepositoryContract
import com.fibelatti.pigbank.data.goal.SavingsDataModel
import com.fibelatti.pigbank.data.goal.SavingsRepositoryContract
import com.fibelatti.pigbank.data.userpreferences.UserPreferencesDataModel
import com.fibelatti.pigbank.data.userpreferences.UserPreferencesRepositoryContract

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

//region Initial Data
const val INITIAL_DATA_USER_PREFERENCES_TABLE = "$INSERT_OR_REPLACE_INTO ${UserPreferencesDataModel.TABLE_NAME}" +
    " $VALUES ${UserPreferencesDataModel.TABLE_INITIAL_VALUES}"

//endregion

@Database(
    entities = [GoalDataModel::class, SavingsDataModel::class, UserPreferencesDataModel::class],
    version = DATABASE_CURRENT_VERSION,
    exportSchema = false)
@TypeConverters(AppTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getGoalRepository(): GoalRepositoryContract
    abstract fun getSavingsRepository(): SavingsRepositoryContract
    abstract fun getUserPreferencesRepository(): UserPreferencesRepositoryContract

    object RoomCallback : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            db.execSQL(INITIAL_DATA_USER_PREFERENCES_TABLE)
        }
    }
}
