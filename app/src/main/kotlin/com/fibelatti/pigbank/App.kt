package com.fibelatti.pigbank

import android.app.Application
import android.support.v4.app.FragmentActivity
import com.fibelatti.pigbank.di.component.AppComponent
import com.fibelatti.pigbank.di.component.DaggerAppComponent
import com.fibelatti.pigbank.di.component.GoalDetailComponent
import com.fibelatti.pigbank.di.component.GoalsComponent
import com.fibelatti.pigbank.di.component.PreferencesComponent
import com.fibelatti.pigbank.di.module.AppModule
import com.fibelatti.pigbank.di.module.GoalDetailModule
import com.fibelatti.pigbank.di.module.GoalsModule
import com.fibelatti.pigbank.di.module.PreferencesModule

class App : Application() {
    companion object {
        lateinit var instance: App

        lateinit var appComponent: AppComponent
    }

    private var goalsComponent: GoalsComponent? = null
    private var goalDetailComponent: GoalDetailComponent? = null
    private var preferencesComponent: PreferencesComponent? = null

    override fun onCreate() {
        super.onCreate()
        instance = this
        buildComponents()
    }

    private fun buildComponents() {
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }

    fun instantiateGoalsComponent(activity: FragmentActivity): GoalsComponent? {
        if (goalsComponent == null) {
            goalsComponent = appComponent.instantiateComponent(GoalsModule(activity))
        }

        return goalsComponent
    }

    fun releaseGoalsComponent() {
        goalsComponent = null
    }

    fun instantiateGoalDetailComponent(activity: FragmentActivity): GoalDetailComponent? {
        if (goalDetailComponent == null) {
            goalDetailComponent = appComponent.instantiateComponent(GoalDetailModule(activity))
        }

        return goalDetailComponent
    }

    fun releaseGoalDetailComponent() {
        goalDetailComponent = null
    }

    fun instantiatePreferencesComponent(activity: FragmentActivity): PreferencesComponent? {
        if (preferencesComponent == null) {
            preferencesComponent = appComponent.instantiateComponent(PreferencesModule(activity))
        }

        return preferencesComponent
    }

    fun releasePreferencesComponent() {
        preferencesComponent = null
    }
}
