package de.lijucay.damier.core.presentation.models

import de.lijucay.damier.core.domain.ReferenceType
import de.lijucay.damier.core.domain.UnitId
import java.time.LocalDate
import java.util.UUID

data class ActivityUi(
    val id: UUID,
    val title: String,
    val unitId: UnitId,
    val reference: Int,
    val referenceType: ReferenceType,
    val defaultAmount: Int,
    val groupedCheckIns: Map<LocalDate, List<CheckInUi>>,
    val streaks: List<StreakUi>
)
