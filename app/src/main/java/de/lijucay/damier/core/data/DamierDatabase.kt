package de.lijucay.damier.core.data

import androidx.room.Database
import androidx.room.RoomDatabase
import de.lijucay.damier.core.data.daos.ActivityInfoDao
import de.lijucay.damier.core.data.daos.CheckInDao
import de.lijucay.damier.core.data.daos.StreakDao
import de.lijucay.damier.core.data.entities.ActivityInfo
import de.lijucay.damier.core.data.entities.CheckInInfo
import de.lijucay.damier.core.data.entities.Streak
import de.lijucay.damier.core.domain.DataUtil

@Database(
    entities = [
        ActivityInfo::class,
        CheckInInfo::class,
        Streak::class
    ],
    version = DataUtil.DATABASE_SCHEME_VERSION,
    exportSchema = false
)
abstract class DamierDatabase : RoomDatabase() {
    abstract fun activityInfoDao(): ActivityInfoDao
    abstract fun checkInDao(): CheckInDao
    abstract fun streakDao(): StreakDao
}