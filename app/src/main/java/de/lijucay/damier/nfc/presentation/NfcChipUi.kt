package de.lijucay.damier.nfc.presentation

import de.lijucay.damier.core.data.converter.UUIDSerializer
import de.lijucay.damier.core.presentation.models.DisplayableDateTime
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class NfcChipUi(
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    @Serializable(with = UUIDSerializer::class) val activityId: UUID,
    val linkedAt: DisplayableDateTime,
    val label: String?
)
