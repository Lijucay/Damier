package de.lijucay.damier.activity.domain

import de.lijucay.damier.check_in.domain.model.CheckInDomain
import de.lijucay.damier.streak.domain.StreakDomain
import de.lijucay.damier.nfc.domain.NfcChipDomain
import de.lijucay.damier.shared.ReferenceType
import de.lijucay.damier.shared.UnitId
import java.time.LocalTime
import java.util.UUID

data class ActivityDomain(
    val id: UUID,
    val title: String,
    val unitId: UnitId,
    val reference: Int,
    val referenceType: ReferenceType,
    val defaultAmount: Int,
    val checkIns: List<CheckInDomain>,
    val streaks: List<StreakDomain>,
    val nfcChipId: List<NfcChipDomain>,
    val remindersEnabled: Boolean,
    val reminderTime: LocalTime?
)