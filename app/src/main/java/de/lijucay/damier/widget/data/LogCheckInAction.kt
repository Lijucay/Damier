package de.lijucay.damier.widget.data

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.updateAll
import de.lijucay.damier.core.data.entities.CheckInInfo
import de.lijucay.damier.core.domain.ActivityRepository
import de.lijucay.damier.widget.domain.WidgetActivityState
import de.lijucay.damier.widget.domain.WidgetRepository
import de.lijucay.damier.widget.presentation.DamierWidget
import kotlinx.coroutines.flow.first
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
        val activity = widgetRepository.observeActivity(activityId).first()

        repository.upsertCheckIn(
            CheckInInfo(
                activityId = activityId,
                timestamp = LocalDateTime.now(),
                amount = (activity as? WidgetActivityState.Loaded)?.activity?.activityInfo?.defaultAmount ?: 0
            )
        )

        DamierWidget().updateAll(context)
    }

    companion object {
        val ACTIVITY_ID_KEY = ActionParameters.Key<String>("activityId")
    }
}