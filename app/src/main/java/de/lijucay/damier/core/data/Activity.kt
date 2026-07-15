package de.lijucay.damier.core.data

import androidx.room.Embedded
import androidx.room.Relation
import de.lijucay.damier.core.data.entities.ActivityInfo
import de.lijucay.damier.core.data.entities.CheckInInfo
import de.lijucay.damier.core.data.entities.NfcChipInfo
import de.lijucay.damier.core.data.entities.Streak

data class Activity(
    @Embedded val activityInfo: ActivityInfo,
    @Relation(
        parentColumn = "id",
        entityColumn = "activityId"
    )
    val checkIns: List<CheckInInfo>,
    @Relation(
        parentColumn = "id",
        entityColumn = "activityId"
    )
    val streaks: List<Streak>,
    @Relation(
        parentColumn = "id",
        entityColumn = "activityId"
    )
    val chips: List<NfcChipInfo>
)
