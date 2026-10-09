package de.lijucay.damier.nfc.presentation

import de.lijucay.damier.core.presentation.models.toDisplayableDateTime
import de.lijucay.damier.nfc.domain.NfcChipDomain

fun NfcChipDomain.toNfcChipUi(): NfcChipUi {
    return NfcChipUi(
        id = id,
        activityId = activityId,
        linkedAt = linkedAt.toDisplayableDateTime(),
        label = label
    )
}

fun List<NfcChipDomain>.toNfcChipUis() = this.map { it.toNfcChipUi() }

fun NfcChipUi.toNfcChipDomain(): NfcChipDomain {
    return NfcChipDomain(
        id = id,
        activityId = activityId,
        linkedAt = linkedAt.value,
        label = label
    )
}

fun List<NfcChipUi>.toNfcChipDomain() = this.map { it.toNfcChipDomain() }