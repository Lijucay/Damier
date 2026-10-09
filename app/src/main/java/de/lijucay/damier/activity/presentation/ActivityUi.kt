package de.lijucay.damier.activity.presentation

import de.lijucay.damier.check_in.presentation.CheckInGroupUi
import de.lijucay.damier.core.data.converter.UUIDSerializer
import de.lijucay.damier.core.presentation.models.DisplayableTime
import de.lijucay.damier.nfc.presentation.NfcChipUi
import de.lijucay.damier.streak.presentation.StreakUi
import de.lijucay.damier.shared.ReferenceType
import de.lijucay.damier.shared.UnitId
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class ActivityUi(
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    val title: String,
    val unitId: UnitId,
    val reference: Int,
    val referenceType: ReferenceType,
    val defaultAmount: Int,
    val groupedCheckIns: List<CheckInGroupUi>,
    val streaks: List<StreakUi>,
    val nfcChipId: List<NfcChipUi>,
    val remindersEnabled: Boolean,
    val reminderTime: DisplayableTime?
)