package com.haroldadmin.moonshot.notifications

import android.app.Notification
import android.content.Context
import androidx.core.app.NotificationManagerCompat
import androidx.preference.PreferenceManager
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.haroldadmin.moonshot.notifications.workers.ScheduleWorker
import com.haroldadmin.moonshot.utils.log
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class RealLaunchNotificationsManager @Inject constructor(private val appContext: Context) : LaunchNotificationsManager {

    override fun enable() {
        log("Enqueueing scheduling work")
        WorkManager
            .getInstance(appContext)
            .enqueueUniquePeriodicWork(ScheduleWorker.name, ExistingPeriodicWorkPolicy.KEEP, schedulingWorkRequest)
    }

    override fun schedule(type: LaunchNotification, time: Long) {
        type.schedule(appContext, time)
    }

    override fun disable(type: LaunchNotification) {
        log("Disabling notifications")
        cancelIntents(type)
        // TODO Investigate if disabling a single notification also disables the other type
        WorkManager.getInstance(appContext).cancelAllWorkByTag(ScheduleWorker.name)
    }

    override fun notify(type: LaunchNotification, notification: Notification) {
        val settings = PreferenceManager.getDefaultSharedPreferences(appContext)
        if (settings.getBoolean(type.enabledPreferenceKey, true)) {
            NotificationManagerCompat
                .from(appContext)
                .notify(type.notificationId, notification)
        }
    }

    private val constraints: Constraints
        get() = Constraints.Builder()
            .setRequiresDeviceIdle(false)
            .setRequiresBatteryNotLow(false)
            .setRequiresStorageNotLow(false)
            .setRequiresCharging(false)
            .build()

    private val schedulingWorkRequest: PeriodicWorkRequest
        get() = PeriodicWorkRequestBuilder<ScheduleWorker>(6, TimeUnit.HOURS)
            .setConstraints(constraints)
            .addTag(ScheduleWorker.name)
            .build()

    private fun cancelIntents(type: LaunchNotification) = with(type) {
        cancelIntent(appContext)
        removeNotifications(appContext)
    }
}