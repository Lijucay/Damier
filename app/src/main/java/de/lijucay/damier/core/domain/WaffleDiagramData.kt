package de.lijucay.damier.core.domain

import de.lijucay.damier.core.presentation.models.CheckInUi

data class WaffleDiagramData(
    val reference: Int,
    val referenceType: ReferenceType,
    val checkIns: List<CheckInUi>
)