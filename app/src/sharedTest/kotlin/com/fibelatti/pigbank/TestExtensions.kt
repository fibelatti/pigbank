package com.fibelatti.pigbank

import io.reactivex.observers.TestObserver
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.reactivestreams.Subscription

/***
 * Mockito.any() returns null and that can be an issue when testing Kotlin code.
 * This function addresses that issue and enables the usage of this matcher.
 */
fun <T> any(): T {
    Mockito.any<T>()
    return uninitialized()
}

@SuppressWarnings("unchecked")
private fun <T> uninitialized(): T = null as T

/***
 * Whenever testing hot observable (something coming from an Rx operation such as merge, concat, etc.)
 * we need to mock the initial subscription call otherwise the test would hand and fail.
 *
 * This provides a TestObserver capable of dealing with those scenarios.
 *
 * @see <a href="https://github.com/ReactiveX/RxJava/wiki/What%27s-different-in-2.0#mockito--testsubscriber">Mockito & TestSubscriber</a>
 */
@SuppressWarnings("unchecked")
fun <T> hotTestObserver(): TestObserver<T> {
    val mockedTestObserver: TestObserver<T> = mock(TestObserver::class.java) as TestObserver<T>

    Mockito.doAnswer { invocation ->
        val subscription = invocation.getArgument<Subscription>(0)
        subscription.request(java.lang.Long.MAX_VALUE)
        return@doAnswer null
    }.`when`(mockedTestObserver).onSubscribe(any())

    return mockedTestObserver
}
