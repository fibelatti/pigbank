package com.fibelatti.pigbank.di.component

import com.fibelatti.pigbank.di.module.AppModule
import com.fibelatti.pigbank.di.scope.AppScope
import dagger.Component

@Component(
    modules = [AppModule::class]
)
@AppScope
interface AppComponent
