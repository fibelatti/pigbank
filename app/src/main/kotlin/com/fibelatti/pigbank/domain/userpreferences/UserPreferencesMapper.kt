package com.fibelatti.pigbank.domain.userpreferences

import com.fibelatti.pigbank.data.userpreferences.USER_PREFERENCE_NAME_ANALYTICS_ENABLED
import com.fibelatti.pigbank.data.userpreferences.USER_PREFERENCE_NAME_CRASH_REPORTS_ENABLED
import com.fibelatti.pigbank.data.userpreferences.USER_PREFERENCE_NAME_FIRST_GOAL_HINT_DISMISSED
import com.fibelatti.pigbank.data.userpreferences.USER_PREFERENCE_NAME_QUICK_SAVE_HINT_DISMISSED
import com.fibelatti.pigbank.data.userpreferences.UserPreferencesEntity
import javax.inject.Inject

class UserPreferencesMapper @Inject constructor() {
    fun toDomainModel(userPreferencesEntity: List<UserPreferencesEntity>): UserPreferencesModel = with(userPreferencesEntity) {
        val analyticsEnabled = firstOrNull { it.name == USER_PREFERENCE_NAME_ANALYTICS_ENABLED }?.value
        val crashReportsEnabled = firstOrNull { it.name == USER_PREFERENCE_NAME_CRASH_REPORTS_ENABLED }?.value
        val firstGoalHintDismissed = firstOrNull { it.name == USER_PREFERENCE_NAME_FIRST_GOAL_HINT_DISMISSED }?.value
        val quickSaveHintDismissed = firstOrNull { it.name == USER_PREFERENCE_NAME_QUICK_SAVE_HINT_DISMISSED }?.value

        return@with UserPreferencesModel(
            analyticsEnabled = analyticsEnabled?.toBoolean() ?: false,
            crashReportsEnabled = crashReportsEnabled?.toBoolean() ?: false,
            firstGoalHintDismissed = firstGoalHintDismissed?.toBoolean() ?: true,
            quickSaveHintDismissed = quickSaveHintDismissed?.toBoolean() ?: true
        )
    }
}
