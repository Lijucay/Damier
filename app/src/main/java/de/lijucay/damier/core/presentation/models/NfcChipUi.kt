package de.lijucay.damier.core.presentation.models

import de.lijucay.damier.core.data.converter.LocalDateTimeSerializer
import de.lijucay.damier.core.data.converter.UUIDSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.util.UUID

@Serializable
data class NfcChipUi(
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    @Serializable(with = UUIDSerializer::class) val activityId: UUID,
    @Serializable(with = LocalDateTimeSerializer::class) val linkedAt: LocalDateTime,
    val label: String?
)
