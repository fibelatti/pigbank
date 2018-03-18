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

    @Test
    fun testIsDate() {
        assertTrue("20/08/1991".isDate())
        assertFalse("".isDate())
    }

    @Test
    fun testIsValidDate() {
        assertTrue("20/08/1991".isValidDate())
        assertFalse("30/02/2018".isValidDate())
    }
}
