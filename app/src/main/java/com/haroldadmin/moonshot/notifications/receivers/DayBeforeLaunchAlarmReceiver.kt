package com.haroldadmin.moonshot.notifications.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.haroldadmin.moonshot.base.notify
import com.haroldadmin.moonshot.core.invoke
import com.haroldadmin.moonshot.models.DatePrecision
import com.haroldadmin.moonshot.notifications.LaunchNotification
import com.haroldadmin.moonshot.notifications.LaunchNotificationContent
import com.haroldadmin.moonshot.notifications.LaunchNotificationsManager
import com.haroldadmin.moonshot.utils.format
import com.haroldadmin.moonshot.utils.log
import com.haroldadmin.moonshotRepository.launch.GetNextLaunchUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.core.qualifier.named
import kotlin.coroutines.CoroutineContext

class DayBeforeLaunchAlarmReceiver : BroadcastReceiver(), KoinComponent, CoroutineScope {

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
    }

    override val coroutineContext: CoroutineContext = Dispatchers.Default + exceptionHandler

    private val notification by inject<LaunchNotification>(named("day-before-launch"))
    private val repository by inject<GetNextLaunchUseCase>()

    override fun onReceive(context: Context, intent: Intent) {
        launch {
            val nextLaunch = repository
                .getNextLaunch()
                .first()
                .invoke()
                .takeIf { it != null && it.maxPrecision == DatePrecision.day }
                ?: run {
                    log("Could not get launch for next day from database. Not scheduling notifications")
                    return@launch
                }

            val content = LaunchNotificationContent(
                name = nextLaunch.missionName,
                site = nextLaunch.siteName ?: "Unknown",
                date = nextLaunch.launchDate?.format(
                    context.resources.configuration,
                    DatePrecision.hour.dateFormat
                ),
                missionPatch = nextLaunch.missionPatch,
                flightNumber = nextLaunch.flightNumber,
                time = nextLaunch.launchDate.time
            )

            notification
                .create(context, content)
                .notify(context, LaunchNotificationsManager.DAY_BEFORE_LAUNCH_NOTIFICATION_ID)
        }
    }
}