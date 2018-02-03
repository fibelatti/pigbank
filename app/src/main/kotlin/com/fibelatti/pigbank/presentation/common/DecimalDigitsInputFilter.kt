package com.fibelatti.pigbank.presentation.common

import android.text.InputFilter
import android.text.Spanned
import java.util.regex.Pattern

private const val DIGITS_BEFORE_ZERO = 9
private const val DIGITS_AFTER_ZERO = 2

class DecimalDigitsInputFilter(digitsBeforeZero: Int = DIGITS_BEFORE_ZERO, digitsAfterZero: Int = DIGITS_AFTER_ZERO) : InputFilter {
    private val pattern: Pattern = Pattern.compile("[0-9]{0," + (digitsBeforeZero - 1) + "}+((\\.[0-9]{0," + (digitsAfterZero - 1) + "})?)||(\\.)?")

    override fun filter(source: CharSequence, start: Int, end: Int, dest: Spanned, destStart: Int, destEnd: Int): CharSequence? =
        if (!pattern.matcher(dest).matches()) "" else null
}
