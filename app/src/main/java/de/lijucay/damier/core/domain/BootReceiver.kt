package de.lijucay.damier.core.domain

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
        Logger.i(context, "Received intent: ${intent.action}")
        if (intent.action != Intent.ACTION_BOOT_COMPLETED) {
            Logger.i(context, "Intent was not ACTION_BOOT_COMPLETED, ignore")
            return
        }

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