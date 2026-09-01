package de.lijucay.damier.core

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import de.lijucay.damier.widget.presentation.DamierWidget
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class BootReceiver : BroadcastReceiver(), KoinComponent {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED) return

        val pendingResult = goAsync()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                DamierWidget().updateOnReboot(context)
            } catch (e: Exception) {
                Logger.e(context, e.message ?: "Unknown error", e)
            } finally {
                pendingResult.finish()
            }
        }
    }
}