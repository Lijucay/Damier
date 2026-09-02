package de.lijucay.damier.core.data.nfc

import java.util.UUID

data class NfcLabelState(
    val currentNfcTagId: UUID? = null,
    val currentLabel: String = ""
)
