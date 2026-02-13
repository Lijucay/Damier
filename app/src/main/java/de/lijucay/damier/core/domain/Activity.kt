package de.lijucay.damier.core.domain

import java.util.UUID

data class Activity(
    val id: UUID,
    val title: String,
    val unitId: UnitId,
    val reference: Int,
    val referenceType: ReferenceType
)