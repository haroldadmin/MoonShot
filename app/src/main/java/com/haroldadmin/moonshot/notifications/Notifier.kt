package com.haroldadmin.moonshot.notifications

import android.annotation.TargetApi
import android.app.Notification
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
import androidx.core.os.bundleOf
import androidx.navigation.NavDeepLinkBuilder
import coil.Coil
import coil.api.get
import com.haroldadmin.moonshot.R
import com.haroldadmin.moonshot.core.unsyncedLazy
import com.haroldadmin.moonshot.models.DatePrecision
import com.haroldadmin.moonshot.models.NotificationType
import com.haroldadmin.moonshot.models.launch.Launch
import com.haroldadmin.moonshot.models.launch.missionPatch
import com.haroldadmin.moonshot.notifications.NotificationConstants.DayBeforeLaunch
import com.haroldadmin.moonshot.notifications.NotificationConstants.JustBeforeLaunch
import com.haroldadmin.moonshot.notifications.NotificationConstants.ScheduleChange
import com.haroldadmin.moonshotRepository.launch.GetNextLaunchUseCase
import com.haroldadmin.moonshotRepository.notifications.NotificationRecordsUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.joda.time.LocalDateTime
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject
import javax.inject.Named
import kotlin.coroutines.CoroutineContext

/**
 * Responsible for posting notifications whenever requested from a Broadcast Receiver.
 *
 * Notifier checks if the requested notification type is enabled in settings, and if not,
 * then does no-op.
 */
@ExperimentalCoroutinesApi
class Notifier @Inject constructor(
    private val context: Context,
    private val nextLaunchUseCase: GetNextLaunchUseCase,
    private val notifRecordsUseCase: NotificationRecordsUseCase,
    @Named("settings") val settings: SharedPreferences
) : CoroutineScope {

    override val coroutineContext: CoroutineContext = Dispatchers.Main + Job()

    private val notificationManager: NotificationManagerCompat by unsyncedLazy {
        NotificationManagerCompat.from(context)
    }

    fun processBroadcast(notificationType: NotificationType) {
        launch {
            when (notificationType) {
                NotificationType.JustBeforeLaunch -> processJustBeforeLaunchBroadcast()
                NotificationType.DayBeforeLaunch -> processDayBeforeLaunchBroadcast()
                NotificationType.ScheduleChange, NotificationType.Unknown -> Unit
            }
        }
    }

    /**
     * Notifications should not be posted in the following scenarios:
     * 1. Disabled in settings
     * 2. Failure to fetch data from Database AND Network
     * 3. When unable to fetch new launch details from the network (Although this is debatable)
     *
     * When retrieving the next launch, the data should be divided into a segregated data type consisting of
     * both cached and refreshed launches. If the cached and refreshed launches are different launches,
     */
    private suspend fun processJustBeforeLaunchBroadcast() {
        val isNotifEnabled = settings.getBoolean(JustBeforeLaunch.settingsKey, true)
        if (!isNotifEnabled) {
            return
        }

        val (cachedLaunch, newlyFetchedLaunch) = nextLaunchUseCase.getNextLaunch().segregate()

        if (cachedLaunch == null && newlyFetchedLaunch == null) {
            return
        }

        when {
            cachedLaunch == null -> {
                newlyFetchedLaunch!! // We've already checked that it is not null

                if (newlyFetchedLaunch.tentativeMaxPrecision != DatePrecision.hour) {
                    // Launch time is uncertain, don't notify the user
                    return
                }

                if (notifRecordsUseCase.hasNotifiedForLaunch(
                        newlyFetchedLaunch.flightNumber,
                        NotificationType.JustBeforeLaunch
                    )
                ) {
                    // Notification for this launch has already been posted, don't notify again.
                    return
                }

                notifyForLaunch(newlyFetchedLaunch, NotificationType.JustBeforeLaunch)

                notifRecordsUseCase.recordNotification(
                    newlyFetchedLaunch.flightNumber,
                    Date(),
                    NotificationType.JustBeforeLaunch
                )
            }
            newlyFetchedLaunch == null -> {
                // Sync failure, notify user?
                // TODO Figure out appropriate action for this scenario
            }
            cachedLaunch != newlyFetchedLaunch -> {
                // Launch Schedule has changed, notify about changed schedule
                if (cachedLaunch.flightNumber == newlyFetchedLaunch.flightNumber) {
                    notifyForScheduleChange(cachedLaunch, newlyFetchedLaunch)
                    notifRecordsUseCase.recordNotification(
                        newlyFetchedLaunch.flightNumber,
                        Date(),
                        NotificationType.ScheduleChange
                    )

                    val paddingTimeMins = settings.getInt(JustBeforeLaunch.paddingKey, 30)
                    val isWithinNotificationRange = LocalDateTime(newlyFetchedLaunch.launchDateUtc).isBefore(
                        LocalDateTime.now().plusMinutes(paddingTimeMins)
                    )
                    if (isWithinNotificationRange) {
                        notifyForLaunch(newlyFetchedLaunch, NotificationType.JustBeforeLaunch)
                        notifRecordsUseCase.recordNotification(
                            newlyFetchedLaunch.flightNumber,
                            Date(),
                            NotificationType.JustBeforeLaunch
                        )
                    }
                } else {
                    // A new launch has been made the next launch. If it is within notification range, notify about it.
                    val paddingTimeMins = settings.getInt(JustBeforeLaunch.paddingKey, 30)
                    val isWithinNotificationRange = LocalDateTime(newlyFetchedLaunch.launchDateUtc).isBefore(
                        LocalDateTime.now().plusMinutes(paddingTimeMins)
                    )

                    if (isWithinNotificationRange) {
                        notifyForLaunch(newlyFetchedLaunch, NotificationType.JustBeforeLaunch)
                        notifRecordsUseCase.recordNotification(
                            newlyFetchedLaunch.flightNumber,
                            Date(),
                            NotificationType.JustBeforeLaunch
                        )
                    }
                }
            }
            else -> {
                // Launch schedule is unchanged, notify about it
                notifyForLaunch(newlyFetchedLaunch, NotificationType.JustBeforeLaunch)
                notifRecordsUseCase.recordNotification(
                    newlyFetchedLaunch.flightNumber,
                    Date(),
                    NotificationType.JustBeforeLaunch
                )
            }
        }
    }

    private suspend fun processDayBeforeLaunchBroadcast() {
        val isNotifEnabled = settings.getBoolean(DayBeforeLaunch.settingsKey, true)
        if (!isNotifEnabled) {
            return
        }

        val (cachedLaunch, newlyFetchedLaunch) = nextLaunchUseCase.getNextLaunch().segregate()

        if (cachedLaunch == null && newlyFetchedLaunch == null) {
            return
        }

        when {
            cachedLaunch == null -> {
                newlyFetchedLaunch!!
                if (newlyFetchedLaunch.tentativeMaxPrecision != DatePrecision.hour) {
                    return
                }
                if (notifRecordsUseCase.hasNotifiedForLaunch(newlyFetchedLaunch.flightNumber, NotificationType.DayBeforeLaunch)) {
                    return
                }
                notifyForLaunch(newlyFetchedLaunch, NotificationType.DayBeforeLaunch)
                notifRecordsUseCase.recordNotification(
                    newlyFetchedLaunch.flightNumber,
                    Date(),
                    NotificationType.DayBeforeLaunch
                )
            }
            newlyFetchedLaunch == null -> {
                // TODO Figure out how to handle this scenario
            }
            cachedLaunch != newlyFetchedLaunch -> {
                if (cachedLaunch.flightNumber == newlyFetchedLaunch.flightNumber) {
                    notifyForScheduleChange(cachedLaunch, newlyFetchedLaunch)
                    notifRecordsUseCase.recordNotification(
                        newlyFetchedLaunch.flightNumber,
                        Date(),
                        NotificationType.ScheduleChange
                    )
                } else {
                    val paddingTimeMins = settings.getInt(JustBeforeLaunch.paddingKey, 30)
                    val isWithinNotificationRange = LocalDateTime(newlyFetchedLaunch.launchDateUtc).isBefore(
                        LocalDateTime.now().plusMinutes(paddingTimeMins)
                    )
                    if (isWithinNotificationRange) {
                        notifyForLaunch(newlyFetchedLaunch, NotificationType.JustBeforeLaunch)
                        notifRecordsUseCase.recordNotification(
                            newlyFetchedLaunch.flightNumber,
                            Date(),
                            NotificationType.JustBeforeLaunch
                        )
                    }
                }
            }
            else -> {
                notifyForLaunch(newlyFetchedLaunch, NotificationType.DayBeforeLaunch)
                notifRecordsUseCase.recordNotification(
                    newlyFetchedLaunch.flightNumber,
                    Date(),
                    NotificationType.JustBeforeLaunch
                )
            }
        }
    }

    private suspend fun notifyForLaunch(launch: Launch, notificationType: NotificationType) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createLaunchNotificationChannel(notificationType)
        }
        val notification = createLaunchNotification(launch, notificationType)
        val notificationId = when (notificationType) {
            NotificationType.JustBeforeLaunch -> JustBeforeLaunch.notificationId
            NotificationType.DayBeforeLaunch -> DayBeforeLaunch.notificationId
            NotificationType.ScheduleChange, NotificationType.Unknown -> error(
                "Can not create launch notification for notification type: $notificationType"
            )
        }
        notificationManager.notify(notificationId, notification)
    }

    private suspend fun notifyForScheduleChange(cachedLaunch: Launch, newlyFetchedLaunch: Launch) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createScheduleChangeNotificationChannel()
        }
        val notification = createScheduleChangeNotification(cachedLaunch, newlyFetchedLaunch)
        notificationManager.notify(ScheduleChange.notificationId, notification)
    }

    private suspend fun createLaunchNotification(
        launch: Launch,
        notificationType: NotificationType
    ): Notification {
        val missionPatch = launch.missionPatch(small = true)?.let { url ->
            Coil.get(url).toBitmap()
        } ?: Coil.get(R.drawable.ic_round_rocket_small).toBitmap()

        val formattedLaunchDate = formatNotificationDate(
            context,
            launch.launchDateUtc,
            launch.tentativeMaxPrecision.dateFormat
        )

        return when (notificationType) {
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
                    .setContentIntent(createPendingIntent(context, launch.flightNumber))
                    .setCategory(NotificationCompat.CATEGORY_REMINDER)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .build()
            }
            NotificationType.DayBeforeLaunch -> {
                NotificationCompat.Builder(context, DayBeforeLaunch.channelId)
                    .setSmallIcon(R.drawable.ic_round_rocket_small)
                    .setLargeIcon(missionPatch)
                    .setContentTitle(context.getString(R.string.launchNotificationContentTitle, launch.missionName))
                    .setContentText(
                        context.getString(
                            R.string.dayBeforeLaunchNotificationContentText,
                            formattedLaunchDate
                        )
                    )
                    .setContentIntent(createPendingIntent(context, launch.flightNumber))
                    .setCategory(NotificationCompat.CATEGORY_REMINDER)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .build()
            }
            else -> error("Unknown launch type received for creating notifications: $notificationType")
        }
    }

    private suspend fun createScheduleChangeNotification(
        cachedLaunch: Launch,
        newlyFetchedLaunch: Launch
    ): Notification {
        val missionPatch = newlyFetchedLaunch.missionPatch(small = true)?.let { url ->
            Coil.get(url).toBitmap()
        } ?: Coil.get(R.drawable.ic_round_rocket_small).toBitmap()

        val newFormattedDate = formatNotificationDate(context, newlyFetchedLaunch.launchDateUtc)

        return NotificationCompat.Builder(context, ScheduleChange.channelId)
            .setSmallIcon(R.drawable.ic_round_rocket_small)
            .setLargeIcon(missionPatch)
            .setContentTitle(context.getString(R.string.scheduleChangeContentTitle))
            .setContentText(
                context.getString(
                    R.string.scheduleChangeContentText,
                    newlyFetchedLaunch.missionName,
                    newFormattedDate
                )
            )
            .setContentIntent(createPendingIntent(context, newlyFetchedLaunch.flightNumber))
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .build()
    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun createLaunchNotificationChannel(notificationType: NotificationType) = when (notificationType) {
        NotificationType.JustBeforeLaunch -> {
            val channelId = JustBeforeLaunch.channelId
            val channel = NotificationChannel(
                channelId,
                context.getString(R.string.justBeforeLaunchChannelName),
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }
        NotificationType.DayBeforeLaunch -> {
            val channelId = DayBeforeLaunch.channelId
            val channel = NotificationChannel(
                channelId,
                context.getString(R.string.dayBeforeLaunchChannelName),
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }
        NotificationType.ScheduleChange, NotificationType.Unknown -> Unit
    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun createScheduleChangeNotificationChannel() {
        val channel = NotificationChannel(
            ScheduleChange.channelId,
            context.getString(R.string.scheduleChangeChannelDescription),
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)
    }

    private fun formatNotificationDate(
        context: Context,
        date: Date,
        pattern: String = DatePrecision.day.dateFormat
    ): String {
        val locale = ConfigurationCompat.getLocales(context.resources.configuration)[0]
        val formatter = SimpleDateFormat(pattern, locale)
        return formatter.format(date)
    }

    private fun createPendingIntent(context: Context, flightNumber: Int): PendingIntent {
        return NavDeepLinkBuilder(context)
            .setGraph(R.navigation.nav_graph)
            .setDestination(R.id.launchDetails)
            .setArguments(bundleOf("flightNumber" to flightNumber))
            .createPendingIntent()
    }
}