package de.lijucay.damier.core.domain.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.glance.appwidget.updateAll
import de.lijucay.damier.widget.presentation.DamierWidget

class DamierWidgetRebootHelper : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            DamierWidget().updateOnReboot(context)
        }
    }
}