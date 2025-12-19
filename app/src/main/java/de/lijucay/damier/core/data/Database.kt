package de.lijucay.damier.core.data

import androidx.room.Database
import androidx.room.RoomDatabase
import de.lijucay.damier.core.data.entities.ActivityInfo
import de.lijucay.damier.core.data.entities.CheckInInfo

@Database(
    entities = [ActivityInfo::class, CheckInInfo::class],
    version = 1,
    exportSchema = false
)
abstract class Database : RoomDatabase() {
}