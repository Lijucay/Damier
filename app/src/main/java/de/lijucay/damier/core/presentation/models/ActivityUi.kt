package de.lijucay.damier.core.presentation.models

import de.lijucay.damier.core.domain.ReferenceType
import java.util.UUID

data class ActivityUi(
    val id: UUID,
    val title: String,
    val singularUnit: String,
    val pluralUnit: String,
    val reference: Int,
    val referenceType: ReferenceType
)
