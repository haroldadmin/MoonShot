package com.haroldadmin.moonshot.notifications.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.haroldadmin.moonshot.base.notify
import com.haroldadmin.moonshot.models.LONG_DATE_FORMAT
import com.haroldadmin.moonshot.notifications.LaunchNotification
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
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.joda.time.LocalDate
import org.koin.core.qualifier.named
import kotlin.coroutines.CoroutineContext

class WeekBeforeLaunchAlarmReceiver : BroadcastReceiver(), KoinComponent, CoroutineScope {

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
    }
    override val coroutineContext: CoroutineContext = Dispatchers.Default + exceptionHandler

    private val notification by inject<LaunchNotification>(named("week-before-launch"))
    private val repository by inject<LaunchesRepository>()

    override fun onReceive(context: Context, intent: Intent) {
        launch {
            val start = LocalDate.now(DateTimeZone.UTC).toDateTimeAtStartOfDay()
            val end = start.plusDays(7)

            val notificationContent = repository
                .getLaunchesInTimeRangeFromDatabase(
                    start = start.millis,
                    end = end.millis,
                    limit = 5
                )
                .takeIf { it.isNotEmpty() }
                ?.map { launch ->
                    LaunchNotificationContent(
                        name = launch.missionName,
                        site = launch.launchSite?.siteName ?: "Unknown",
                        date = launch.launchDate.format(
                            context.resources.configuration,
                            LONG_DATE_FORMAT
                        ),
                        missionPatch = launch.missionPatch,
                        flightNumber = launch.flightNumber,
                        time = launch.launchDate.time
                    )
                }
                ?.toTypedArray()
                ?: return@launch log("No launches this week, not scheduling notifications")

            notificationContent.forEach { content ->
                notification
                    .create(context, content)
                    .notify(context, LaunchNotificationsManager.WEEK_BEFORE_LAUNCH_NOTIFICATION_ID)
            }
        }
    }
}