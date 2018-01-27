package com.fibelatti.pigbank.di.module

import android.app.Application
import android.arch.persistence.room.Room
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.fibelatti.pigbank.data.localdatasource.AppDatabase
import com.fibelatti.pigbank.data.localdatasource.DATABASE_NAME
import com.fibelatti.pigbank.di.qualifier.AppQualifier
import com.fibelatti.pigbank.di.scope.AppScope
import dagger.Module
import dagger.Provides
import java.util.Locale

@Module
class AppModule(private var app: Application) {

    @Provides
    fun provideApplication(): Application = app

    @Provides
    @AppQualifier
    fun provideContext(): Context = app.applicationContext

    @Provides
    fun provideSharedPreferences(): SharedPreferences = PreferenceManager.getDefaultSharedPreferences(app)

    @Provides
    fun provideLocaleDefault(): Locale = Locale.getDefault()

    @Provides
    @AppScope
    fun providesDatabase(@AppQualifier context: Context) = Room.databaseBuilder(context,
        AppDatabase::class.java, DATABASE_NAME)
        .fallbackToDestructiveMigration()
        .build()
}
