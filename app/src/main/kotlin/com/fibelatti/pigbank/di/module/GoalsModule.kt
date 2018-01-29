package com.fibelatti.pigbank.di.module

import android.content.Context
import android.support.v4.app.FragmentActivity
import dagger.Module
import dagger.Provides

@Module
class GoalsModule(private val activity: FragmentActivity) {

    @Provides
    fun provideContext(): Context = activity.baseContext

    @Provides
    fun provideActivity(): FragmentActivity = activity
}
