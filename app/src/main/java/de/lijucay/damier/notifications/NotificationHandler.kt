package de.lijucay.damier.notifications

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.VISIBILITY_PUBLIC
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.work.WorkManager
import de.lijucay.damier.MainActivity
import de.lijucay.damier.R
import de.lijucay.damier.core.domain.Logger
import de.lijucay.damier.core.domain.DamierConstants
import java.util.UUID

object NotificationHandler {
    fun notificationId(activityId: UUID): Int = activityId.mostSignificantBits.toInt()

    fun workName(activityId: UUID): String = "reminder_$activityId"

    fun cancelReminder(context: Context, activityId: UUID) {
        NotificationManagerCompat.from(context).cancel(notificationId(activityId))
        WorkManager.getInstance(context).cancelUniqueWork(workName(activityId))
    }

    fun createReminderNotification(
        context: Context,
        activityId: UUID,
        activityTitle: String
    ) : Boolean {
        val manager = NotificationManagerCompat.from(context)
        if (!manager.areNotificationsEnabled()) return false

        val notificationId = notificationId(activityId)

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
            putExtra(DamierConstants.Extras.ACTIVITY_ID, activityId.toString())
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            notificationId,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        createNotificationChannel(context)

        val notification = NotificationCompat.Builder(context, DamierConstants.Channels.ACTIVITY_REMINDER)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(context.getString(R.string.activity_reminder_title, activityTitle))
            .setContentText(context.getString(R.string.activity_reminder_text, activityTitle))
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setVisibility(VISIBILITY_PUBLIC)
            .build()

        return try {
            manager.notify(notificationId, notification)
            true
        } catch (e: SecurityException) {
            Logger.e(context, e.message ?: "Unknown issue", e)
            false
        }
    }

    private fun createNotificationChannel(context: Context) {
        val channel = NotificationChannelCompat.Builder(
            DamierConstants.Channels.ACTIVITY_REMINDER,
            NotificationManagerCompat.IMPORTANCE_HIGH
        )
            .setName(context.getString(R.string.reminder_channel_name))
            .setDescription(context.getString(R.string.reminder_channel_description))
            .build()

        NotificationManagerCompat.from(context).createNotificationChannel(channel)
    }

    fun checkNotificationPermission(context: Context): Boolean {
        val manager = NotificationManagerCompat.from(context)
        if (!manager.areNotificationsEnabled()) return false

        val channel = manager.getNotificationChannel(DamierConstants.Channels.ACTIVITY_REMINDER)
        return channel == null || channel.importance != NotificationManagerCompat.IMPORTANCE_NONE
    }

    fun isPermissionPermanentlyDenied(context: Context): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return false
        val activity = context as? ComponentActivity ?: return false
        val isGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
        val showRationale = ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.POST_NOTIFICATIONS)

        return !isGranted && !showRationale
    }

    fun openAppNotificationSettings(context: Context) {
        context.startActivity(
            Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        )
    }
}