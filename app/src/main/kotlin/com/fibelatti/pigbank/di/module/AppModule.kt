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
import com.fibelatti.pigbank.presentation.common.providers.AppSchedulerProvider
import com.fibelatti.pigbank.presentation.common.providers.SchedulerProvider
import com.fibelatti.pigbank.presentation.common.providers.AppResourceProvider
import com.fibelatti.pigbank.presentation.common.providers.ResourceProvider
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
    fun provideLocaleDefault(): Locale = Locale.getDefault()

    @Provides
    fun provideResourceProvider(context: Context): ResourceProvider = AppResourceProvider(context)

    @Provides
    fun provideSchedulerProvider(): SchedulerProvider = AppSchedulerProvider()

    @Provides
    @AppScope
    fun providesDatabase(context: Context) = Room.databaseBuilder(context,
        AppDatabase::class.java, DATABASE_NAME)
        .fallbackToDestructiveMigration()
        .build()
}
