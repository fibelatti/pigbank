package com.fibelatti.pigbank.common

inline fun <T, R> T?.ifNotNullThisElseThat(ifNotNull: (T) -> R, ifNull: () -> R) {
    if (this != null) ifNotNull(this) else ifNull()
}
