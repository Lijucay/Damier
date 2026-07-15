package de.lijucay.damier.core.presentation.models

import de.lijucay.damier.core.domain.models.NfcChipDomain

fun NfcChipDomain.toNfcChipUi(): NfcChipUi {
    return NfcChipUi(
        id = id,
        activityId = activityId,
        linkedAt = linkedAt,
        label = label
    )
}

fun List<NfcChipDomain>.toNfcChipUis() = this.map { it.toNfcChipUi() }

fun NfcChipUi.toNfcChipDomain(): NfcChipDomain {
    return NfcChipDomain(
        id = id,
        activityId = activityId,
        linkedAt = linkedAt,
        label = label
    )
}

fun List<NfcChipUi>.toNfcChipDomain() = this.map { it.toNfcChipDomain() }