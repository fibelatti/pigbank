package com.fibelatti.pigbank.di.module

import android.app.Application
import android.content.Context
import com.fibelatti.pigbank.di.module.AppModule.Binder
import com.fibelatti.pigbank.di.scope.AppScope
import com.fibelatti.pigbank.external.providers.AppResourceProvider
import com.fibelatti.pigbank.external.providers.AppSchedulerProvider
import com.fibelatti.pigbank.external.providers.ResourceProvider
import com.fibelatti.pigbank.external.providers.SchedulerProvider
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module(includes = [Binder::class])
class AppModule {
    @Module
    interface Binder {
        @Binds
        fun provideApplication(app: Application): Context
    }

    @Provides
    @AppScope
    fun provideResourceProvider(context: Context): ResourceProvider = AppResourceProvider(context)

    @Provides
    @AppScope
    fun provideSchedulerProvider(): SchedulerProvider = AppSchedulerProvider()
}
