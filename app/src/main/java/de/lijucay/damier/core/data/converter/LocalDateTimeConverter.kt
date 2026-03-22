package de.lijucay.damier.core.data.converter

import androidx.room.TypeConverter
import java.time.LocalDateTime

class LocalDateTimeConverter {
    @TypeConverter
    fun toDateTime(dateTimeString: String): LocalDateTime {
        return LocalDateTime.parse(dateTimeString)
    }

    @TypeConverter
    fun toString(dateTime: LocalDateTime): String {
        return dateTime.toString()
    }
}