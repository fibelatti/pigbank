package com.fibelatti.pigbank.data.userpreferences

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import com.fibelatti.pigbank.data.userpreferences.UserPreferencesEntity.Companion.TABLE_NAME
import com.fibelatti.pigbank.data.userpreferences.UserPreferencesType.UNKNOWN

private const val USER_PREFERENCE_TYPE_TOGGLE = "toggle"
private const val USER_PREFERENCE_TYPE_HINT = "hint"

const val USER_PREFERENCE_NAME_ANALYTICS_ENABLED = "Analytics Enabled"
const val USER_PREFERENCE_NAME_CRASH_REPORTS_ENABLED = "Crash Reports Enabled"
const val USER_PREFERENCE_NAME_FIRST_GOAL_HINT_DISMISSED = "First Goal Hint Dismissed"
const val USER_PREFERENCE_NAME_QUICK_SAVE_HINT_DISMISSED = "Quick Save Hint Dismissed"

@Entity(tableName = TABLE_NAME)
data class UserPreferencesEntity(
    @ColumnInfo(name = COLUMN_ID)
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @ColumnInfo(name = COLUMN_TYPE)
    val type: UserPreferencesType,
    @ColumnInfo(name = COLUMN_NAME)
    val name: String,
    @ColumnInfo(name = COLUMN_VALUE)
    val value: String
) {
    companion object {
        const val TABLE_NAME = "user_preferences"
        const val COLUMN_ID = "_id"
        const val COLUMN_TYPE = "type"
        const val COLUMN_NAME = "name"
        const val COLUMN_VALUE = "value"

        const val TABLE_INITIAL_VALUES = "(0, '$USER_PREFERENCE_TYPE_TOGGLE', '$USER_PREFERENCE_NAME_ANALYTICS_ENABLED', 'true')," +
            "(1, '$USER_PREFERENCE_TYPE_TOGGLE', '$USER_PREFERENCE_NAME_CRASH_REPORTS_ENABLED', 'true')," +
            "(2, '$USER_PREFERENCE_TYPE_HINT', '$USER_PREFERENCE_NAME_FIRST_GOAL_HINT_DISMISSED', 'false')," +
            "(3, '$USER_PREFERENCE_TYPE_HINT', '$USER_PREFERENCE_NAME_QUICK_SAVE_HINT_DISMISSED', 'false')"
    }

    @Ignore constructor() : this(
        id = 0,
        type = UNKNOWN,
        name = "",
        value = ""
    )
}

enum class UserPreferencesType(val value: String) {
    UNKNOWN("unknown"), TOGGLE(USER_PREFERENCE_TYPE_TOGGLE), HINT(USER_PREFERENCE_TYPE_HINT);

    companion object {
        private val map = UserPreferencesType.values().associateBy(UserPreferencesType::value)
        fun fromString(value: String) = map[value] ?: UNKNOWN
    }
}
