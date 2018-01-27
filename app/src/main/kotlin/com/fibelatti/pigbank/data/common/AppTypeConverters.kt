package com.fibelatti.pigbank.data.common

import android.arch.persistence.room.TypeConverter
import java.util.Date

class AppTypeConverters {
    @TypeConverter
    fun fromDate(date: Date?): Long? = date?.time

    @TypeConverter
    fun toDate(millisSinceEpoch: Long?): Date? = if (millisSinceEpoch == null) {
        null
    } else Date(millisSinceEpoch)
}
