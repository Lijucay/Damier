package de.lijucay.damier.core.domain

import android.content.Intent
import java.util.UUID

sealed interface IntentAction {
    data class OpenActivityDetails(val id: UUID) : IntentAction
    data object AddActivity : IntentAction
}

fun resolveIntentAction(intent: Intent?): IntentAction? {
    return when {
        intent?.action == "de.lijucay.damier.action.ADD_ACTIVITY" ->
            IntentAction.AddActivity

        intent?.getStringExtra("activityId") != null ->
            intent.getStringExtra("activityId")
                ?.let { runCatching { UUID.fromString(it) }.getOrNull() }
                ?.let { IntentAction.OpenActivityDetails(it) }

        else -> null
    }
}