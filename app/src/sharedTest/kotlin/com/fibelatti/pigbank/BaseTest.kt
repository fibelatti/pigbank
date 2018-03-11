package com.fibelatti.pigbank

import com.fibelatti.pigbank.external.providers.ResourceProvider
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.mockito.BDDMockito.given
import org.mockito.Mockito.anyInt
import org.mockito.Mockito.mock

const val GENERIC_ERROR_MESSAGE = "Error"
const val GENERIC_STRING_RESOURCE = "Generic string resource"

abstract class BaseTest {
    protected val testSchedulerProvider = TestSchedulerProvider()
    protected val testResourceProvider: ResourceProvider = mock(ResourceProvider::class.java)

    @Before
    fun setupBase() {
        given(testResourceProvider.getString(anyInt()))
            .willReturn(GENERIC_STRING_RESOURCE)
    }

    protected fun <T> assertCompletableOnComplete(testObserver: TestObserver<T>) {
        testObserver.assertComplete()
        testObserver.assertNoErrors()
    }

    protected fun <T> assertSingleOnCompleteWithNoErrors(testObserver: TestObserver<T>) {
        testObserver.assertComplete()
        testObserver.assertNoErrors()
        testObserver.assertValueCount(1)
    }

    protected fun mockedRunnable(body: Runnable) {
        body.run()
    }
}
