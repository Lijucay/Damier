package de.lijucay.damier.check_in.presentation

import de.lijucay.damier.core.data.converter.UUIDSerializer
import de.lijucay.damier.core.presentation.models.DisplayableDateTime
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class CheckInUi(
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    @Serializable(with = UUIDSerializer::class) val activityId: UUID,
    val dateTime: DisplayableDateTime,
    val amount: Int,
    val note: String?
)