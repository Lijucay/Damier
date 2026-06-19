package de.lijucay.damier.widget.presentation

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.state.updateAppWidgetState
import de.lijucay.damier.core.DataPreferences

class ClearDeletedActivityAction : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        updateAppWidgetState(context, glanceId) { prefs ->
            prefs.remove(DataPreferences.Keys.activityId)
            prefs.remove(DataPreferences.Keys.activityName)
        }
        DamierWidget().update(context, glanceId)
    }
}