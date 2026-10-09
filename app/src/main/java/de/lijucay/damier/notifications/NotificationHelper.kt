package de.lijucay.damier.notifications

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import de.lijucay.damier.core.domain.Logger
import de.lijucay.damier.core.domain.DamierConstants
import java.time.Duration
import java.time.LocalTime
import java.time.ZonedDateTime
import java.util.UUID
import java.util.concurrent.TimeUnit

class NotificationHelper(private val context: Context) {
    fun scheduleNotification(
        activityId: UUID,
        activityTitle: String,
        time: LocalTime
    ) {
        Logger.i(context, "Reminder scheduling request received for activity $activityTitle (id: $activityId) for ${time.hour}:${time.minute}")

        val now = ZonedDateTime.now()
        var target = now.with(time).withSecond(0).withNano(0)

        if (!target.isAfter(now)) target = target.plusDays(1)
        val delayMs = Duration.between(now, target).toMillis()

        Logger.i(context, "Scheduling reminder notification for $activityTitle (id: $activityId) in ${delayMs}ms")

        val data = workDataOf(
            DamierConstants.Extras.ACTIVITY_ID to activityId.toString(),
            DamierConstants.Extras.ACTIVITY_TITLE to activityTitle,
            DamierConstants.Extras.REMINDER_TIME to time.toString()
        )

        val request = OneTimeWorkRequestBuilder<ReminderNotificationWorker>()
            .addTag(DamierConstants.Workers.ACTIVITY_REMINDER)
            .addTag("activity_$activityId")
            .setInitialDelay(delayMs, TimeUnit.MILLISECONDS)
            .setInputData(data)
            .setBackoffCriteria(BackoffPolicy.LINEAR, 15, TimeUnit.MINUTES)
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            NotificationHandler.workName(activityId),
            ExistingWorkPolicy.APPEND_OR_REPLACE,
            request
        )
    }

    fun cancelReminder(activityId: UUID) =
        NotificationHandler.cancelReminder(context, activityId)
}