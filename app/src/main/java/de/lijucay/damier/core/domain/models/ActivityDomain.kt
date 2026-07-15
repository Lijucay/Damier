package de.lijucay.damier.core.domain.models

import de.lijucay.damier.shared.ReferenceType
import de.lijucay.damier.shared.UnitId
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
    val nfcChipId: List<NfcChipDomain>
)