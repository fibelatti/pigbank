package com.fibelatti.pigbank.common

import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import org.junit.Test

class ExtensionsTest {

    @Test
    fun testIsFloat() {
        assertTrue("0.00".isFloat())
        assertFalse("".isFloat())
    }
}
