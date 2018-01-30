package com.fibelatti.pigbank.di.component

import android.app.Application
import com.fibelatti.pigbank.App
import com.fibelatti.pigbank.di.module.ActivityBuilderModule
import com.fibelatti.pigbank.di.module.AppModule
import com.fibelatti.pigbank.di.scope.AppScope
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import dagger.android.support.DaggerApplication

@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        ActivityBuilderModule::class,
        AppModule::class]
)
@AppScope
interface AppComponent : AndroidInjector<DaggerApplication> {
    fun inject(app: App)

    override fun inject(instance: DaggerApplication)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}
