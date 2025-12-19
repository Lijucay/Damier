package de.lijucay.damier.core.domain

import java.util.UUID

data class Activity(
    val id: UUID,
    val title: String,
    val description: String,
    val singularUnit: String,
    val pluralUnit: String,
    val reference: Int,
    val referenceType: ReferenceType
)