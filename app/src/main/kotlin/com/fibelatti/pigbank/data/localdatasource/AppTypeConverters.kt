package com.fibelatti.pigbank.data.localdatasource

import android.arch.persistence.room.TypeConverter
import com.fibelatti.pigbank.data.userpreferences.UserPreferencesType
import java.util.Date

class AppTypeConverters {
    @TypeConverter
    fun fromDate(date: Date?): Long? = date?.time

    @TypeConverter
    fun toDate(millisSinceEpoch: Long?): Date? = if (millisSinceEpoch == null) {
        null
    } else Date(millisSinceEpoch)

    @TypeConverter
    fun fromUserPreferencesType(type: UserPreferencesType): String = type.value

    @TypeConverter
    fun toUserPreferencesType(value: String): UserPreferencesType = UserPreferencesType.fromString(value)
}
