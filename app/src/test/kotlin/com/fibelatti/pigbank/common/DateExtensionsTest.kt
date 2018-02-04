package com.fibelatti.pigbank.common

import com.fibelatti.pigbank.common.DateHelper.DATE_FORMAT_STRING
import junit.framework.Assert.assertEquals
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DateExtensionsTest {

    @Test
    fun testStringAsDate() {
        assertEquals(Date(1517702400000), stringAsDate("04/02/2018"))
    }

    @Test
    fun testIntPartsAsDate() {
        assertEquals(Date(1517702400000), intPartsAsDate(2018, 1, 4))
    }

    @Test
    fun intPartsAsDateString() {
        assertEquals("04/02/2018", intPartsAsDateString(2018, 1, 4))
    }

    @Test
    fun testDateAsStringWithDateFormat() {
        assertEquals("04/02/2018", Date(1517702400000).asString(SimpleDateFormat(DATE_FORMAT_STRING, Locale.US)))
    }

    @Test
    fun testDateAsStringWithStringFormat() {
        assertEquals("04/02/2018", Date(1517702400000).asString(DATE_FORMAT_STRING))
    }

    @Test
    fun testDateAsString() {
        assertEquals("04/02/2018", Date(1517702400000).asString())
    }

    @Test
    fun testLongAsString() {
        assertEquals("04/02/2018", 1517702400000.asDateString())
    }
}
