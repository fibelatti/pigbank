package com.fibelatti.pigbank.presentation.rateapp

import com.fibelatti.pigbank.external.providers.ResourceProvider
import com.fibelatti.pigbank.external.providers.SchedulerProvider
import com.fibelatti.pigbank.presentation.base.BasePresenter
import com.fibelatti.pigbank.presentation.rateapp.RateAppContract.View

private const val minPositiveRating = 4

class RateAppPresenter(
    schedulerProvider: SchedulerProvider,
    resourceProvider: ResourceProvider
) : RateAppContract.Presenter, BasePresenter<RateAppContract.View>(schedulerProvider, resourceProvider) {

    private var view: RateAppContract.View? = null

    override fun attachView(view: View) {
        super.attachView(view)
        this.view = view
    }

    override fun ratingChanged(value: Int) {
        if (value >= minPositiveRating) view?.showPlayStore() else view?.showEmail()
    }
}
