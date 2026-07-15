package de.lijucay.damier.core.data.wrapper

import de.lijucay.damier.core.data.entities.NfcChipInfo
import de.lijucay.damier.core.domain.models.NfcChipDomain

fun NfcChipInfo.toNfcChipDomain(): NfcChipDomain {
    return NfcChipDomain(
        id = chipId,
        activityId = activityId,
        linkedAt = linkedAt,
        label = label
    )
}

fun List<NfcChipInfo>.toNfcChipDomains() = this.map { it.toNfcChipDomain() }