package de.lijucay.damier.notifications

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import de.lijucay.damier.core.domain.DamierConstants
import java.time.LocalTime
import java.util.UUID

class ReminderNotificationWorker(
    context: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {
        val activityId = inputData.getString(DamierConstants.Extras.ACTIVITY_ID)
            ?.let { runCatching { UUID.fromString(it) }.getOrNull() }
            ?: return Result.failure()
        val activityTitle = inputData.getString(DamierConstants.Extras.ACTIVITY_TITLE)
            ?: return Result.failure()
        val reminderTime = inputData.getString(DamierConstants.Extras.REMINDER_TIME)
            ?.let { runCatching { LocalTime.parse(it) }.getOrNull() }
            ?: return Result.failure()

        val shown = NotificationHandler.createReminderNotification(
            applicationContext, activityId, activityTitle
        )

        if (!shown && runAttemptCount < 8) return Result.retry()

        if (isStopped) return Result.failure()
        NotificationHelper(applicationContext).scheduleNotification(activityId, activityTitle, reminderTime)
        return if (shown) Result.success() else Result.failure()
    }
}