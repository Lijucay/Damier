package de.lijucay.damier.core.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import de.lijucay.damier.core.data.converter.LocalDateConverter
import de.lijucay.damier.core.data.converter.LocalDateTimeConverter
import de.lijucay.damier.core.data.converter.LocalTimeConverter
import de.lijucay.damier.core.data.converter.ReferenceTypeConverter
import de.lijucay.damier.core.data.converter.UnitIdConverter
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
@TypeConverters(LocalDateTimeConverter::class, LocalDateConverter::class, LocalTimeConverter::class,
    ReferenceTypeConverter::class, UnitIdConverter::class)
abstract class DamierDatabase : RoomDatabase() {
    abstract fun activityInfoDao(): ActivityInfoDao
    abstract fun checkInDao(): CheckInDao
    abstract fun streakDao(): StreakDao
    abstract fun nfcChipDao(): NfcChipDao
}