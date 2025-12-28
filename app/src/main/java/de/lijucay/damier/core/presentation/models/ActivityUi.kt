package de.lijucay.damier.core.presentation.models

import de.lijucay.damier.core.domain.ReferenceType
import de.lijucay.damier.core.domain.UnitId
import java.util.UUID

data class ActivityUi(
    val id: UUID,
    val title: String,
    val unitId: UnitId,
    val reference: Int,
    val referenceType: ReferenceType
)
