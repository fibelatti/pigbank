package com.fibelatti.pigbank.common

inline fun <T> Iterable<T>.sumByFloat(selector: (T) -> Float) = map { selector(it) }.sum()
