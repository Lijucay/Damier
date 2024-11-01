package dev.lijucay.damier.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.lijucay.damier.data.local.model.Habit
import dev.lijucay.damier.data.local.model.TrackingInfo
import dev.lijucay.damier.util.Utils.DATABASE_SCHEME_VERSION

@Database(
    entities = [Habit::class, TrackingInfo::class],
    version = DATABASE_SCHEME_VERSION,
    exportSchema = false
)
abstract class DamierDatabase : RoomDatabase() {
    abstract fun habitDao(): HabitDao
    abstract fun trackingInfoDao(): TrackingInfoDao
}