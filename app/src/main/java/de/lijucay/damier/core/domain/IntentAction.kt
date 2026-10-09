package de.lijucay.damier.core.domain

import android.content.Intent
import android.nfc.NfcAdapter
import java.util.UUID

sealed interface IntentAction {
    data class OpenActivityDetails(val id: UUID) : IntentAction
    data object AddActivity : IntentAction
    data class NFCDetected(val intent: Intent) : IntentAction
}

fun resolveIntentAction(intent: Intent?): IntentAction? {
    val activityId = intent?.getStringExtra(DamierConstants.Extras.ACTIVITY_ID)

    return when {
        intent?.action == DamierConstants.Actions.ADD_ACTIVITY -> IntentAction.AddActivity

        activityId != null -> runCatching { UUID.fromString(activityId) }.getOrNull()
            ?.let { IntentAction.OpenActivityDetails(it) }

        intent?.action == NfcAdapter.ACTION_NDEF_DISCOVERED -> IntentAction.NFCDetected(intent)

        else -> null
    }
}