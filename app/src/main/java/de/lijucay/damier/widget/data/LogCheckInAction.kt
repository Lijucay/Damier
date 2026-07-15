package de.lijucay.damier.widget.data

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback
import de.lijucay.damier.core.data.entities.CheckInInfo
import de.lijucay.damier.core.domain.ActivityRepository
import de.lijucay.damier.widget.domain.WidgetRepository
import de.lijucay.damier.widget.presentation.DamierWidget
import org.koin.core.context.GlobalContext
import java.time.LocalDateTime
import java.util.UUID

class LogCheckInAction : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        val repository: ActivityRepository = GlobalContext.get().get()
        val widgetRepository: WidgetRepository = GlobalContext.get().get()

        val activityId = UUID.fromString(parameters[ACTIVITY_ID_KEY] ?: return)
        val defaultAmount = widgetRepository.getDefaultAmount(activityId)

        repository.upsertCheckIn(
            CheckInInfo(
                activityId = activityId,
                timestamp = LocalDateTime.now(),
                amount = defaultAmount ?: 1
            )
        )

        DamierWidget().update(context, glanceId)
    }

    companion object {
        val ACTIVITY_ID_KEY = ActionParameters.Key<String>("activityId")
    }
}