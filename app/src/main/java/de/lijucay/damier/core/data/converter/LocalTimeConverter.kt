package de.lijucay.damier.core.data.converter

import androidx.room.TypeConverter
import java.time.LocalTime

class LocalTimeConverter {
    @TypeConverter
    fun toLocalTime(timeString: String): LocalTime {
        return LocalTime.parse(timeString)
    }

    @TypeConverter
    fun toString(time: LocalTime): String {
        return time.toString()
    }
}