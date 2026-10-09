package de.lijucay.damier.activity.data.model

import androidx.room.Embedded
import androidx.room.Relation
import de.lijucay.damier.activity.data.model.dto.ActivityInfo
import de.lijucay.damier.check_in.data.model.dto.CheckInInfo
import de.lijucay.damier.nfc.data.model.dto.NfcChipInfo
import de.lijucay.damier.streak.data.model.dto.Streak

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
