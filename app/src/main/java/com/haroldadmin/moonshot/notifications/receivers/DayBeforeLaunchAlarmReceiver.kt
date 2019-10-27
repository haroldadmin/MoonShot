package com.haroldadmin.moonshot.notifications.receivers

import android.content.Context
import android.content.Intent
import com.haroldadmin.moonshot.core.invoke
import com.haroldadmin.moonshot.models.DatePrecision
import com.haroldadmin.moonshot.models.launch.missionPatch
import com.haroldadmin.moonshot.notifications.LaunchNotification
import com.haroldadmin.moonshot.notifications.LaunchNotificationContent
import com.haroldadmin.moonshot.notifications.LaunchNotificationsManager
import com.haroldadmin.moonshot.utils.formatDate
import com.haroldadmin.moonshotRepository.launch.GetNextLaunchUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.first
import org.joda.time.DateTime
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.core.qualifier.named

class DayBeforeLaunchAlarmReceiver : CoroutineBroadcastReceiver(), KoinComponent {

    private val notificationType by inject<LaunchNotification>(named("day-before-launch"))
    private val repository by inject<GetNextLaunchUseCase>()
    private val manager by inject<LaunchNotificationsManager>()

    @FlowPreview
    @ExperimentalCoroutinesApi
    override suspend fun onBroadcastReceived(context: Context, intent: Intent) {
        val now = DateTime.now()
        val tomorrow = now.plusDays(1).millis

        val launchesTillTomorrow = repository
            .getNextLaunchesUntilDate(tomorrow)
            .first()

        launchesTillTomorrow()?.let { launches ->
            launches
                .filter { launch ->
                    launch.tentativeMaxPrecision == DatePrecision.day || launch.tentativeMaxPrecision == DatePrecision.hour
                }
                .map { nextLaunch ->
                    LaunchNotificationContent(
                        name = nextLaunch.missionName,
                        site = nextLaunch.launchSite?.siteName ?: "Unknown",
                        date = context.formatDate(
                            nextLaunch.launchDateUtc,
                            nextLaunch.tentativeMaxPrecision.dateFormat
                        ),
                        missionPatch = nextLaunch.missionPatch(),
                        flightNumber = nextLaunch.flightNumber,
                        time = nextLaunch.launchDateUtc.time
                    )
                }
                .map { content ->
                    notificationType.create(context, content)
                }
                .forEach { notification ->
                    manager.notify(notificationType, notification)
                }
        }
    }
}