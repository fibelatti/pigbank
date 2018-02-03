package com.fibelatti.pigbank.presentation.common.providers

import android.content.Context

class AppResourceProvider(private val context: Context) : ResourceProvider {
    override fun getString(resId: Int): String = context.getString(resId)

    override fun getString(resId: Int, vararg formatArgs: Any): String =
        context.getString(resId, formatArgs)
}
