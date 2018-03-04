package com.fibelatti.pigbank.di.module

import android.app.Application
import android.arch.persistence.room.Room
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.fibelatti.pigbank.data.localdatasource.AppDatabase
import com.fibelatti.pigbank.data.localdatasource.DATABASE_NAME
import com.fibelatti.pigbank.di.module.AppModule.Binder
import com.fibelatti.pigbank.di.scope.AppScope
import com.fibelatti.pigbank.external.providers.AppResourceProvider
import com.fibelatti.pigbank.external.providers.AppSchedulerProvider
import com.fibelatti.pigbank.external.providers.ResourceProvider
import com.fibelatti.pigbank.external.providers.SchedulerProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import java.util.Locale

@Module(includes = [Binder::class])
class AppModule {
    @Module
    interface Binder {
        @Binds
        fun provideApplication(app: Application): Context
    }

    @Provides
    fun provideSharedPreferences(app: Application): SharedPreferences = PreferenceManager.getDefaultSharedPreferences(app)

    @Provides
    @AppScope
    fun provideLocaleDefault(): Locale = Locale.getDefault()

    @Provides
    @AppScope
    fun provideResourceProvider(context: Context): ResourceProvider = AppResourceProvider(context)

    @Provides
    @AppScope
    fun provideSchedulerProvider(): SchedulerProvider = AppSchedulerProvider()

    @Provides
    @AppScope
    fun providesDatabase(context: Context) = Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
        .fallbackToDestructiveMigration()
        .build()
}
