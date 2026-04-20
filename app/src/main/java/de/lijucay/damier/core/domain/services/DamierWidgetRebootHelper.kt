package de.lijucay.damier.core.domain.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class DamierWidgetRebootHelper : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            // TODO: create function "updateOnReboot" for widgets
        }
    }
}