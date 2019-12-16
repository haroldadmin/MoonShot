package com.haroldadmin.moonshot.notifications.receivers

import android.content.Context
import android.content.Intent
import com.haroldadmin.moonshot.core.invoke
import com.haroldadmin.moonshot.core.last
import com.haroldadmin.moonshot.models.DatePrecision
import com.haroldadmin.moonshot.models.launch.missionPatch
import com.haroldadmin.moonshot.notifications.LaunchNotification
import com.haroldadmin.moonshot.notifications.LaunchNotificationContent
import com.haroldadmin.moonshot.notifications.LaunchNotificationsManager
import com.haroldadmin.moonshot.utils.formatDate
import com.haroldadmin.moonshot.utils.log
import com.haroldadmin.moonshotRepository.launch.GetNextLaunchUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.first
import org.joda.time.DateTime
import javax.inject.Inject
import javax.inject.Named

class DayBeforeLaunchAlarmReceiver : CoroutineBroadcastReceiver() {

    @Inject
    @Named("day-before-launch")
    lateinit var notificationType: LaunchNotification

    @Inject
    lateinit var usecase: GetNextLaunchUseCase

    @Inject
    lateinit var manager: LaunchNotificationsManager

    @FlowPreview
    @ExperimentalCoroutinesApi
    override suspend fun onBroadcastReceived(context: Context, intent: Intent) {
        broadcastReceiverComponent.inject(this)
        val now = DateTime.now()
        val tomorrow = now.plusDays(1).millis

        val launchesTillTomorrow = usecase
            .getNextLaunchesUntilDate(tomorrow)
            .last()
            .also { log("Retrieved launches: $it") }

        launchesTillTomorrow()?.let { launches ->
            launches
                .filter { launch ->
                    launch.tentativeMaxPrecision == DatePrecision.day
                            || launch.tentativeMaxPrecision == DatePrecision.hour
                }
                .also {
                    log("Creating notifications for $it")
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