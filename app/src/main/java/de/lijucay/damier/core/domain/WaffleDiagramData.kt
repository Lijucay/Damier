package de.lijucay.damier.core.domain

import de.lijucay.damier.core.presentation.models.CheckInUi
import de.lijucay.damier.shared.ReferenceType

data class WaffleDiagramData(
    val reference: Int,
    val referenceType: ReferenceType,
    val checkIns: List<CheckInUi>
)