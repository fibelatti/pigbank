package com.fibelatti.pigbank.presentation.common

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

private const val DATE_SEPARATOR = "/"
private const val DAY_SEPARATOR_POSITION = 2
private const val MONTH_SEPARATOR_POSITION = 5
private const val FORMATTED_DATE_LENGTH = 10

class DateInputMask(
    private val input: EditText,
    private val separator: String = DATE_SEPARATOR,
    private val daySeparatorPosition: Int = DAY_SEPARATOR_POSITION,
    private val monthSeparatorPosition: Int = MONTH_SEPARATOR_POSITION
) {

    fun listen() {
        input.addTextChangedListener(dateEntryTextWatcher)
    }

    private val dateEntryTextWatcher = object : TextWatcher {
        var edited = false

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            if (edited) {
                edited = false
                return
            }

            var working = getEditText()

            working = manageDateDivider(working, daySeparatorPosition, start, before)
            working = manageDateDivider(working, monthSeparatorPosition, start, before)

            edited = true
            input.setText(working)
            input.setSelection(input.text.length)
        }

        private fun manageDateDivider(working: String, position: Int, start: Int, before: Int): String {
            if (working.length == position) {
                return if (before <= position && start < position) {
                    working + separator
                } else {
                    working.dropLast(1)
                }
            }
            return working
        }

        private fun getEditText(): String = if (input.text.length >= FORMATTED_DATE_LENGTH) {
            input.text.toString().substring(0, FORMATTED_DATE_LENGTH)
        } else {
            input.text.toString()
        }

        override fun afterTextChanged(s: Editable) {}
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
    }
}
