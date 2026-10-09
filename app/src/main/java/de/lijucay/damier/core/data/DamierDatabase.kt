package de.lijucay.damier.core.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import de.lijucay.damier.core.data.converter.LocalDateConverter
import de.lijucay.damier.core.data.converter.LocalDateTimeConverter
import de.lijucay.damier.core.data.converter.LocalTimeConverter
import de.lijucay.damier.core.data.converter.ReferenceTypeConverter
import de.lijucay.damier.core.data.converter.UnitIdConverter
import de.lijucay.damier.activity.data.dao.ActivityInfoDao
import de.lijucay.damier.check_in.data.dao.CheckInDao
import de.lijucay.damier.nfc.data.dao.NfcChipDao
import de.lijucay.damier.streak.data.dao.StreakDao
import de.lijucay.damier.activity.data.model.dto.ActivityInfo
import de.lijucay.damier.check_in.data.model.dto.CheckInInfo
import de.lijucay.damier.nfc.data.model.dto.NfcChipInfo
import de.lijucay.damier.streak.data.model.dto.Streak
import de.lijucay.damier.core.domain.DamierConstants

@Database(
    entities = [
        ActivityInfo::class,
        CheckInInfo::class,
        Streak::class,
        NfcChipInfo::class
    ],
    version = DamierConstants.DATABASE_SCHEME_VERSION,
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