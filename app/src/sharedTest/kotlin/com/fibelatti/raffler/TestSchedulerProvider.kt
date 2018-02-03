package com.fibelatti.raffler

import com.fibelatti.pigbank.presentation.common.providers.SchedulerProvider
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

class TestSchedulerProvider : SchedulerProvider {
    override fun mainThread(): Scheduler = Schedulers.trampoline()

    override fun computation(): Scheduler = Schedulers.trampoline()

    override fun trampoline(): Scheduler = Schedulers.trampoline()

    override fun newThread(): Scheduler = Schedulers.trampoline()

    override fun io(): Scheduler = Schedulers.trampoline()
}
