package com.fibelatti.pigbank.domain.goal.models

import java.util.Date

data class SavingsEntity(
    val id: Long,
    val goalId: Long,
    val amount: Float,
    val date: Date
)
