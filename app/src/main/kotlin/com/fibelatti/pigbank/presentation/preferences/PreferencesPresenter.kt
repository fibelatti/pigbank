package com.fibelatti.pigbank.presentation.preferences

import com.fibelatti.pigbank.external.providers.ResourceProvider
import com.fibelatti.pigbank.external.providers.SchedulerProvider
import com.fibelatti.pigbank.presentation.base.BasePresenter
import com.fibelatti.pigbank.presentation.preferences.PreferencesContract.View

class PreferencesPresenter(
    schedulerProvider: SchedulerProvider,
    resourceProvider: ResourceProvider
) : PreferencesContract.Presenter, BasePresenter<PreferencesContract.View>(schedulerProvider, resourceProvider) {

    private var view: PreferencesContract.View? = null

    override fun attachView(view: View) {
        super.attachView(view)
        this.view = view
    }

    override fun toggleCrashReport(value: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun toggleAnalytics(value: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun resethints() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun shareApp() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun rateApp() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun ratingChanged(value: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
