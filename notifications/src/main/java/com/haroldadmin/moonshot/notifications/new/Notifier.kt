package com.haroldadmin.moonshot.notifications.new

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.os.ConfigurationCompat
import coil.Coil
import coil.api.get
import com.haroldadmin.moonshot.models.DatePrecision
import com.haroldadmin.moonshot.models.NotificationType
import com.haroldadmin.moonshot.models.launch.Launch
import com.haroldadmin.moonshot.models.launch.missionPatch
import com.haroldadmin.moonshot.notifications.R
import com.haroldadmin.moonshot.notifications.new.NotificationConstants.DayBeforeLaunch
import com.haroldadmin.moonshot.notifications.new.NotificationConstants.JustBeforeLaunch
import com.haroldadmin.moonshotRepository.launch.GetNextLaunchUseCase
import com.haroldadmin.moonshotRepository.notifications.NotificationRecordsUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject
import javax.inject.Named
import kotlin.coroutines.CoroutineContext

/**
 * Responsible for posting notifications whenever requested from a Broadcast Receiver.
 */
@ExperimentalCoroutinesApi
class Notifier @Inject constructor(
    private val context: Context,
    private val nextLaunchUseCase: GetNextLaunchUseCase,
    private val notifRecordsUseCase: NotificationRecordsUseCase,
    @Named("settings") val settings: SharedPreferences
) : CoroutineScope {

    override val coroutineContext: CoroutineContext = Dispatchers.Main + Job()

    fun processBroadcast(notificationType: NotificationType) {
        when (notificationType) {
            NotificationType.JustBeforeLaunch -> TODO()
            NotificationType.DayBeforeLaunch -> TODO()
            NotificationType.Unknown -> TODO()
        }
    }

    private suspend fun processJustBeforeLaunchBroadcast() {
        val isNotifEnabled = settings.getBoolean(JustBeforeLaunch.settingsKey, true)
        if (!isNotifEnabled) return

        val (cachedLaunch, newlyFetchedLaunch) = nextLaunchUseCase.getNextLaunch().segregate()

        when {
            cachedLaunch == null -> {
                // Notify using newly fetched launch only
                // Save notification to DB

            }
            newlyFetchedLaunch == null -> {
                // Could not refresh data, notify about missed refresh?
            }
            cachedLaunch != newlyFetchedLaunch -> {
                // Launch Schedule has changed, notify about changed schedule
                // Save notification to DB
            }
            else -> {
                // Launch schedule is unchanged, notify
                // Save notification to DB
            }
        }

    }

    private suspend fun notifyForLaunch(launch: Launch) {
        if (launch.tentativeMaxPrecision != DatePrecision.hour) {
            // Launch time is uncertain, don't notify the user
            return
        }

    }

    private suspend fun createNotification(launch: Launch, notificationType: NotificationType): Unit {
        createChannel(notificationType)

        val missionPatch = launch.missionPatch(small = true)?.let { url ->
            Coil.get(url).toBitmap()
        } ?: Coil.get(R.drawable.ic_round_rocket_small).toBitmap()

        val formattedLaunchDate = formatNotificationDate(
            context,
            launch.launchDateUtc,
            launch.tentativeMaxPrecision.dateFormat
        )

        val notificaton = when (notificationType) {
            NotificationType.JustBeforeLaunch -> {
                NotificationCompat.Builder(context, JustBeforeLaunch.channelId)
                    .setSmallIcon(R.drawable.ic_round_rocket_small)
                    .setLargeIcon(missionPatch)
                    .setContentTitle(context.getString(R.string.launchNotificationContentTitle, launch.missionName))
                    .setContentText(
                        context.getString(
                            R.string.justBeforeLaunchNotificationContentText,
                            formattedLaunchDate
                        )
                    )
//                    .setContentIntent()
            }
            NotificationType.DayBeforeLaunch -> TODO()
            NotificationType.Unknown -> TODO()
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun createChannel(notificationType: NotificationType) = when (notificationType) {
        NotificationType.JustBeforeLaunch -> {
            val channelId = JustBeforeLaunch.channelId
            val channel = NotificationChannel(
                channelId,
                context.getString(R.string.justBeforeLaunchChannelName),
                NotificationManager.IMPORTANCE_HIGH
            )
            NotificationManagerCompat.from(context).createNotificationChannel(channel)
        }
        NotificationType.DayBeforeLaunch -> {
            val channelId = DayBeforeLaunch.channelId
            val channel = NotificationChannel(
                channelId,
                context.getString(R.string.dayBeforeLaunchChannelName),
                NotificationManager.IMPORTANCE_HIGH
            )
            NotificationManagerCompat.from(context).createNotificationChannel(channel)
        }
        NotificationType.Unknown -> Unit
    }

    private fun formatNotificationDate(context: Context, date: Date, pattern: String = DatePrecision.day.dateFormat): String {
        val locale = ConfigurationCompat.getLocales(context.resources.configuration)[0]
        val formatter = SimpleDateFormat(pattern, locale)
        return formatter.format(date)
    }

    private fun createPendingIntent(context: Context, flightNumber: Int): PendingIntent {
//        return NavDeepLinkBuilder(context)
//            .setGraph()
        TODO()
    }
}