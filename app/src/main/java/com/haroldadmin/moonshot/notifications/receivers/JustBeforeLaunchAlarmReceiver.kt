package com.haroldadmin.moonshot.notifications.receivers

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.haroldadmin.moonshot.core.invoke
import com.haroldadmin.moonshot.models.DatePrecision
import com.haroldadmin.moonshot.models.launch.missionPatch
import com.haroldadmin.moonshot.notifications.LaunchNotification
import com.haroldadmin.moonshot.notifications.LaunchNotificationContent
import com.haroldadmin.moonshot.notifications.LaunchNotificationsManager
import com.haroldadmin.moonshot.utils.formatDate
import com.haroldadmin.moonshot.utils.log
import com.haroldadmin.moonshotRepository.launch.GetNextLaunchUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Named

class JustBeforeLaunchAlarmReceiver : CoroutineBroadcastReceiver() {

    @Inject
    @Named("settings")
    lateinit var settings: SharedPreferences

    @Inject
    @Named("just-before-launch")
    lateinit var notificationType: LaunchNotification

    @Inject
    lateinit var repository: GetNextLaunchUseCase

    @Inject
    lateinit var manager: LaunchNotificationsManager

    @ExperimentalCoroutinesApi
    override suspend fun onBroadcastReceived(context: Context, intent: Intent) {
        broadcastReceiverComponent.inject(this)
        val nextLaunch = repository.getNextLaunch()
            .first()
            .invoke()
            .takeIf { it != null && it.tentativeMaxPrecision == DatePrecision.hour }
            ?: run {
                log("Could not get next launch from database. Not scheduling notification")
                return
            }

        val content = LaunchNotificationContent(
            name = nextLaunch.missionName,
            site = nextLaunch.launchSite?.siteName ?: "Unknown",
            date = context.formatDate(nextLaunch.launchDateUtc, nextLaunch.tentativeMaxPrecision.dateFormat),
            missionPatch = nextLaunch.missionPatch(),
            flightNumber = nextLaunch.flightNumber,
            time = nextLaunch.launchDateUtc.time
        )

        val notification = notificationType.create(context, content)
        manager.notify(notificationType, notification)
    }
}