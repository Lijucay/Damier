package de.lijucay.damier.widget.presentation

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.getAppWidgetState
import androidx.glance.state.PreferencesGlanceStateDefinition
import de.lijucay.damier.core.DataPreferences
import java.util.UUID

object DamierWidgetState {
    suspend fun updateWidgetForActivity(context: Context, activityId: UUID) {
        val manager = GlanceAppWidgetManager(context)
        val glanceIds = manager.getGlanceIds(DamierWidget::class.java)

        glanceIds.forEach { glanceId ->
            val prefs = getAppWidgetState(context, PreferencesGlanceStateDefinition, glanceId)
            val widgetActivityId = runCatching {
                UUID.fromString(prefs[DataPreferences.Keys.activityId])
            }.getOrNull()

            if (widgetActivityId == activityId) {
                DamierWidget().update(context, glanceId)
            }
        }
    }
}