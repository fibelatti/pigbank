package com.fibelatti.pigbank.di.module

import android.arch.persistence.room.Room
import android.content.Context
import com.fibelatti.pigbank.data.goal.GoalRepositoryContract
import com.fibelatti.pigbank.data.goal.SavingsRepositoryContract
import com.fibelatti.pigbank.data.localdatasource.AppDatabase
import com.fibelatti.pigbank.data.localdatasource.DATABASE_NAME
import com.fibelatti.pigbank.data.userpreferences.UserPreferencesRepositoryContract
import com.fibelatti.pigbank.di.scope.AppScope
import dagger.Module
import dagger.Provides

@Module
class DatabaseModule {
    @Provides
    @AppScope
    fun provideDatabase(context: Context) = Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
        .addCallback(AppDatabase.RoomCallback)
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    @AppScope
    fun provideGoalRepository(appDatabase: AppDatabase): GoalRepositoryContract =
        appDatabase.getGoalRepository()

    @Provides
    @AppScope
    fun provideSavingsRepository(appDatabase: AppDatabase): SavingsRepositoryContract =
        appDatabase.getSavingsRepository()

    @Provides
    @AppScope
    fun provideUserPreferencesRepository(appDatabase: AppDatabase): UserPreferencesRepositoryContract =
        appDatabase.getUserPreferencesRepository()
}
