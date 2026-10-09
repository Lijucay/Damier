package de.lijucay.damier.nfc.data.wrapper

import de.lijucay.damier.nfc.data.model.dto.NfcChipInfo
import de.lijucay.damier.nfc.domain.NfcChipDomain

fun NfcChipInfo.toNfcChipDomain(): NfcChipDomain {
    return NfcChipDomain(
        id = chipId,
        activityId = activityId,
        linkedAt = linkedAt,
        label = label
    )
}

fun List<NfcChipInfo>.toNfcChipDomains() = this.map { it.toNfcChipDomain() }