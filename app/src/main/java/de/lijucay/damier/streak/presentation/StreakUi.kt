package de.lijucay.damier.streak.presentation

import de.lijucay.damier.core.data.converter.UUIDSerializer
import de.lijucay.damier.core.presentation.models.DisplayableDate
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class StreakUi(
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    @Serializable(with = UUIDSerializer::class) val activityId: UUID,
    val startDate: DisplayableDate,
    val endDate: DisplayableDate,
    val length: Int
)