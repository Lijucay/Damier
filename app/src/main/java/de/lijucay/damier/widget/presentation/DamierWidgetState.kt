package de.lijucay.damier.widget.presentation

import android.content.Context
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.getAppWidgetState
import androidx.glance.state.PreferencesGlanceStateDefinition
import java.util.UUID

object DamierWidgetState {
    val ACTIVITY_ID = stringPreferencesKey("selected_activity_id")
    val ACTIVITY_NAME = stringPreferencesKey("selected_activity_name")

    suspend fun updateWidgetForActivity(context: Context, activityId: UUID) {
        val manager = GlanceAppWidgetManager(context)
        val glanceIds = manager.getGlanceIds(DamierWidget::class.java)

        glanceIds.forEach { glanceId ->
            val prefs = getAppWidgetState(context, PreferencesGlanceStateDefinition, glanceId)
            val widgetActivityId = runCatching {
                UUID.fromString(prefs[ACTIVITY_ID])
            }.getOrNull()

            if (widgetActivityId == activityId) {
                DamierWidget().update(context, glanceId)
            }
        }
    }
}