package com.haroldadmin.moonshot.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.os.bundleOf
import androidx.navigation.NavDeepLinkBuilder
import com.haroldadmin.moonshot.R
import com.haroldadmin.moonshot.utils.GlideApp

class LaunchNotificationBuilder {
    fun create(
        context: Context,
        type: LaunchNotification,
        vararg content: LaunchNotificationContent
    ): List<Notification> {

        createChannels(context)

        return when (type) {
            LaunchNotification.JUST_BEFORE -> {
                listOf(createJustBeforeLaunchNotification(context, content[0]))
            }
            LaunchNotification.DAY_BEFORE -> {
                listOf(createDayBeforeLaunchNotification(context, content[0]))
            }
            LaunchNotification.WEEK_BEFORE -> {
                createWeekBeforeLaunchNotification(context, *content)
            }
        }
    }

    private fun createJustBeforeLaunchNotification(
        context: Context,
        content: LaunchNotificationContent
    ): Notification {
        return NotificationCompat.Builder(
            context,
            LaunchNotificationsManager.JUST_BEFORE_LAUNCH_CHANNEL_ID
        )
            .setSmallIcon(R.drawable.ic_round_rocket_small)
            .setLargeIcon(getMissionPatchIcon(content.missionPatch, context))
            .setContentTitle("${content.name} Launch")
            .setContentText("Launches at ${content.site} on ${content.date}")
            .setContentIntent(getLaunchDetailsPendingIntent(context, content.flightNumber))
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .build()
    }

    private fun createDayBeforeLaunchNotification(
        context: Context,
        content: LaunchNotificationContent
    ): Notification {
        return NotificationCompat.Builder(
            context,
            LaunchNotificationsManager.DAY_BEFORE_LAUNCH_CHANNEL_ID
        )
            .setSmallIcon(R.drawable.ic_round_rocket_small)
            .setLargeIcon(getMissionPatchIcon(content.missionPatch, context))
            .setContentTitle("${content.name} Launch")
            .setContentText("Launches at ${content.site} on ${content.date}")
            .setContentIntent(getLaunchDetailsPendingIntent(context, content.flightNumber))
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .build()
    }

    private fun createWeekBeforeLaunchNotification(
        context: Context,
        vararg contents: LaunchNotificationContent
    ): List<Notification> {

        val notificationList = mutableListOf<Notification>()

        for (content in contents) {
            notificationList += NotificationCompat.Builder(
                context,
                LaunchNotificationsManager.WEEK_BEFORE_LAUNCH_CHANNEL_ID
            )
                .setSmallIcon(R.drawable.ic_round_rocket_small)
                .setLargeIcon(getMissionPatchIcon(content.missionPatch, context))
                .setContentTitle("${content.name} Launch")
                .setContentText("Launches at ${content.site} on ${content.date}")
                .setContentIntent(getLaunchDetailsPendingIntent(context, content.flightNumber))
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setGroup(LaunchNotificationsManager.WEEK_BEFORE_LAUNCH_GROUP_ID)
                .build()
        }

        val notificationStyle = NotificationCompat.InboxStyle().also { style ->
            contents
                .take(6)
                .forEach { content ->
                    style.addLine(content.name)
                }
        }

        notificationList += NotificationCompat.Builder(
            context,
            LaunchNotificationsManager.WEEK_BEFORE_LAUNCH_CHANNEL_ID
        )
            .setSmallIcon(R.drawable.ic_round_rocket_small)
            .setContentTitle("Launches this week")
            .setContentText("There are ${contents.size + 1} launches this week")
            .setStyle(notificationStyle)
            .setGroup(LaunchNotificationsManager.WEEK_BEFORE_LAUNCH_GROUP_ID)
            .setGroupSummary(true)
            .build()

        return notificationList
    }

    private fun getLaunchDetailsPendingIntent(context: Context, flightNumber: Int): PendingIntent {
        return NavDeepLinkBuilder(context)
            .setGraph(R.navigation.nav_graph)
            .setDestination(R.id.launchDetails)
            .setArguments(bundleOf("flightNumber" to flightNumber))
            .createPendingIntent()
    }

    private fun getMissionPatchIcon(url: String?, context: Context): Bitmap {
        return if (url.isNullOrBlank()) {
            GlideApp.with(context)
                .load(R.drawable.ic_round_rocket_small)
                .submit()
                .get()
                .toBitmap()
        } else {
            GlideApp.with(context)
                .asBitmap()
                .load(url)
                .submit()
                .get()
        }
    }

    private fun createChannels(context: Context) {
        val notificationManager = NotificationManagerCompat.from(context)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val justBeforeLaunchChannel =
                NotificationChannel(
                    LaunchNotificationsManager.JUST_BEFORE_LAUNCH_CHANNEL_ID,
                    context.getString(R.string.launchNotificationsJustBeforeLaunchChannelName),
                    NotificationManager.IMPORTANCE_HIGH
                )

            val dayBeforeLaunchChannel =
                NotificationChannel(
                    LaunchNotificationsManager.DAY_BEFORE_LAUNCH_CHANNEL_ID,
                    context.getString(R.string.launchNotificationsDayBeforeLaunchChannelName),
                    NotificationManager.IMPORTANCE_DEFAULT
                )

            val weekBeforeLaunchChannel =
                NotificationChannel(
                    LaunchNotificationsManager.WEEK_BEFORE_LAUNCH_CHANNEL_ID,
                    context.getString(R.string.launchNotificationsWeekBeforeLaunchChannelName),
                    NotificationManager.IMPORTANCE_DEFAULT
                )

            notificationManager.createNotificationChannel(justBeforeLaunchChannel)
            notificationManager.createNotificationChannel(dayBeforeLaunchChannel)
            notificationManager.createNotificationChannel(weekBeforeLaunchChannel)
        }
    }
}