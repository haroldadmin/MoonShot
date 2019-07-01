package com.haroldadmin.moonshot.notifications.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.haroldadmin.moonshot.base.notify
import com.haroldadmin.moonshot.models.LONG_DATE_FORMAT
import com.haroldadmin.moonshot.notifications.LaunchNotification
import com.haroldadmin.moonshot.notifications.LaunchNotificationBuilder
import com.haroldadmin.moonshot.notifications.LaunchNotificationContent
import com.haroldadmin.moonshot.notifications.LaunchNotificationsManager
import com.haroldadmin.moonshot.utils.format
import com.haroldadmin.moonshot.utils.log
import com.haroldadmin.moonshotRepository.launch.LaunchesRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.joda.time.DateTimeZone
import org.joda.time.LocalDate
import org.koin.core.KoinComponent
import org.koin.core.inject
import kotlin.coroutines.CoroutineContext

class JustBeforeLaunchAlarmReceiver : BroadcastReceiver(), KoinComponent, CoroutineScope {

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
    }
    override val coroutineContext: CoroutineContext = Dispatchers.Default + exceptionHandler

    private val notificationBuilder by inject<LaunchNotificationBuilder>()
    private val repository by inject<LaunchesRepository>()

    override fun onReceive(context: Context, intent: Intent) {
        launch {
            val timestamp = LocalDate
                .now(DateTimeZone.UTC)
                .toDateTimeAtStartOfDay()
                .millis

            val nextLaunch = repository
                .getNextLaunchFromDatabase(timestamp)
                ?: run {
                    log("Could not get next launch from database. Not scheduling notification")
                    return@launch
                }

            val notificationContent = LaunchNotificationContent(
                name = nextLaunch.missionName,
                site = nextLaunch.launchSite?.siteName ?: "Unknown",
                date = nextLaunch.launchDate.format(
                    context.resources.configuration,
                    LONG_DATE_FORMAT
                ),
                missionPatch = nextLaunch.missionPatch,
                flightNumber = nextLaunch.flightNumber,
                time = nextLaunch.launchDate.time
            )

            notificationBuilder
                .create(context, LaunchNotification.JUST_BEFORE, notificationContent)
                .map { notification ->
                    notification.notify(context, LaunchNotificationsManager.JUST_BEFORE_LAUNCH_NOTIFICATION_ID)
                }
        }
    }
}