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
import com.haroldadmin.moonshot.utils.log
import com.haroldadmin.moonshotRepository.launch.GetNextLaunchUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.core.qualifier.named

class JustBeforeLaunchAlarmReceiver : CoroutineBroadcastReceiver(), KoinComponent {

    private val repository by inject<GetNextLaunchUseCase>()
    private val notificationType by inject<LaunchNotification>(named("just-before-launch"))
    private val manager by inject<LaunchNotificationsManager>()

    @ExperimentalCoroutinesApi
    override suspend fun onBroadcastReceived(context: Context, intent: Intent) {
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