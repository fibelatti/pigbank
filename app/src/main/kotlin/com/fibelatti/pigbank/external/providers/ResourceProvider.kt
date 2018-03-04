package com.fibelatti.pigbank.external.providers

import android.support.annotation.StringRes

interface ResourceProvider {
    fun getString(@StringRes resId: Int): String

    fun getString(@StringRes resId: Int, vararg formatArgs: Any): String
}
