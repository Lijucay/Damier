package de.lijucay.damier.nfc

import java.util.UUID

data class NfcLabelState(
    val currentNfcTagId: UUID? = null,
    val currentLabel: String = ""
)
