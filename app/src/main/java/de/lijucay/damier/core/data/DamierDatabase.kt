package de.lijucay.damier.core.data

import androidx.room.Database
import androidx.room.RoomDatabase
import de.lijucay.damier.core.data.daos.ActivityInfoDao
import de.lijucay.damier.core.data.daos.CheckInDao
import de.lijucay.damier.core.data.daos.NfcChipDao
import de.lijucay.damier.core.data.daos.StreakDao
import de.lijucay.damier.core.data.entities.ActivityInfo
import de.lijucay.damier.core.data.entities.CheckInInfo
import de.lijucay.damier.core.data.entities.NfcChipInfo
import de.lijucay.damier.core.data.entities.Streak
import de.lijucay.damier.core.domain.DataUtil

@Database(
    entities = [
        ActivityInfo::class,
        CheckInInfo::class,
        Streak::class,
        NfcChipInfo::class
    ],
    version = DataUtil.DATABASE_SCHEME_VERSION,
    exportSchema = true
)
abstract class DamierDatabase : RoomDatabase() {
    abstract fun activityInfoDao(): ActivityInfoDao
    abstract fun checkInDao(): CheckInDao
    abstract fun streakDao(): StreakDao
    abstract fun nfcChipDao(): NfcChipDao
}