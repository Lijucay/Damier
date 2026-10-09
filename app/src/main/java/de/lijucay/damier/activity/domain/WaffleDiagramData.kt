package de.lijucay.damier.activity.domain

import de.lijucay.damier.check_in.presentation.CheckInUi
import de.lijucay.damier.shared.ReferenceType

data class WaffleDiagramData(
    val reference: Int,
    val referenceType: ReferenceType,
    val checkIns: List<CheckInUi>
)