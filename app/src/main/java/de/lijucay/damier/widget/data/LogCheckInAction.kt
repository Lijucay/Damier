package de.lijucay.damier.widget.data

import android.appwidget.AppWidgetManager
import android.content.Context
import android.widget.RemoteViews
import androidx.glance.ExperimentalGlanceApi
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.runComposition
import de.lijucay.damier.core.Logger
import de.lijucay.damier.core.data.entities.CheckInInfo
import de.lijucay.damier.core.domain.ActivityRepository
import de.lijucay.damier.widget.domain.WidgetRepository
import de.lijucay.damier.widget.presentation.DamierWidget
import kotlinx.coroutines.withTimeoutOrNull
import org.koin.core.context.GlobalContext
import java.time.LocalDateTime
import java.util.UUID
import kotlin.time.Duration.Companion.milliseconds

class LogCheckInAction : ActionCallback {
    @OptIn(ExperimentalGlanceApi::class)
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        Logger.d(context, "onAction called")
        val repository: ActivityRepository = GlobalContext.get().get()
        val widgetRepository: WidgetRepository = GlobalContext.get().get()

        val activityId = UUID.fromString(parameters[ACTIVITY_ID_KEY] ?: return)
        Logger.d(context, "Received activityId = $activityId")

        val defaultAmount = widgetRepository.getDefaultAmount(activityId)

        Logger.d(context, "Received defaultAmount = $defaultAmount")
        repository.upsertCheckIn(
            CheckInInfo(
                activityId = activityId,
                timestamp = LocalDateTime.now(),
                amount = defaultAmount ?: 1
            )
        )

        Logger.d(context, "Room finished writing $defaultAmount to $activityId")

        try {
            var remoteViews: RemoteViews? = null
            withTimeoutOrNull(1000.milliseconds) {
                DamierWidget().runComposition(context, glanceId).collect { latest ->
                    remoteViews = latest
                }
            }

            val widgetView = remoteViews
            if (widgetView != null) {
                val appWidgetId = GlanceAppWidgetManager(context).getAppWidgetId(glanceId)
                AppWidgetManager.getInstance(context).updateAppWidget(appWidgetId, widgetView)
            }
        } catch (_: Exception) {
            // Fallback
            DamierWidget().update(context, glanceId)
        }

        Logger.d(context, "Damier Widget updated")
    }

    companion object {
        val ACTIVITY_ID_KEY = ActionParameters.Key<String>("activityId")
    }
}