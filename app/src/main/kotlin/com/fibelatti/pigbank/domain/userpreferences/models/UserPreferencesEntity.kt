package com.fibelatti.pigbank.domain.userpreferences.models

data class UserPreferencesEntity(
    val analyticsEnabled: Boolean,
    val crashReportsEnabled: Boolean,
    val firstGoalHintDismissed: Boolean,
    val quickSaveHintDismissed: Boolean
)
