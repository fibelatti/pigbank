package com.fibelatti.pigbank.common

inline fun <T> Iterable<T>.sumByFloat(selector: (T) -> Float) = map { selector(it) }.sum()

fun String.isFloat(): Boolean = try {
    this.toFloat()
    true
} catch (e: Exception) {
    false
}
