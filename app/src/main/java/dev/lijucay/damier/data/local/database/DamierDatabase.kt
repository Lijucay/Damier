package dev.lijucay.damier.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.lijucay.damier.data.local.model.Habit
import dev.lijucay.damier.data.local.model.TrackingInfo

@Database(
    entities = [Habit::class, TrackingInfo::class],
    version = 1,
    exportSchema = false
)
abstract class DamierDatabase : RoomDatabase() {
    abstract fun habitDao(): HabitDao
    abstract fun trackingInfoDao(): TrackingInfoDao
}