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
import com.haroldadmin.moonshot.utils.GlideRequests

sealed class LaunchNotification : MoonShotNotification {

    fun getMissionPatch(url: String?, glide: GlideRequests): Bitmap {
        return if (url.isNullOrBlank()) {
            glide.load(R.drawable.ic_round_rocket_small)
                .submit()
                .get()
                .toBitmap()
        } else {
            glide.asBitmap()
                .load(url)
                .submit()
                .get()
        }
    }
}

object JustBeforeLaunch : LaunchNotification() {
    override fun create(context: Context, content: LaunchNotificationContent): Notification {
        val glide = GlideApp.with(context)
        val icon = getMissionPatch(content.missionPatch, glide)

        createChannel(context)

        return NotificationCompat.Builder(
            context,
            LaunchNotificationsManager.JUST_BEFORE_LAUNCH_CHANNEL_ID
        )
            .setSmallIcon(R.drawable.ic_round_rocket_small)
            .setLargeIcon(icon)
            .setContentTitle("${content.name} Launch")
            .setContentText("Launches at ${content.site} on ${content.date}")
            .setContentIntent(getLaunchDetailsPendingIntent(context, content.flightNumber))
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .build()
    }

    private fun getLaunchDetailsPendingIntent(context: Context, flightNumber: Int): PendingIntent {
        return NavDeepLinkBuilder(context)
            .setGraph(R.navigation.nav_graph)
            .setDestination(R.id.launchDetails)
            .setArguments(bundleOf("flightNumber" to flightNumber))
            .createPendingIntent()
    }

    override fun createChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                LaunchNotificationsManager.JUST_BEFORE_LAUNCH_CHANNEL_ID,
                context.getString(R.string.launchNotificationsJustBeforeLaunchChannelName),
                NotificationManager.IMPORTANCE_HIGH
            )
            NotificationManagerCompat.from(context)
                .createNotificationChannel(channel)
        }
    }
}

object DayBeforeLaunch : LaunchNotification() {
    override fun create(context: Context, content: LaunchNotificationContent): Notification {
        createChannel(context)

        return NotificationCompat.Builder(
            context,
            LaunchNotificationsManager.DAY_BEFORE_LAUNCH_CHANNEL_ID
        )
            .setSmallIcon(R.drawable.ic_round_rocket_small)
            .setLargeIcon(getMissionPatch(content.missionPatch, GlideApp.with(context)))
            .setContentTitle("${content.name} Launch")
            .setContentText("Launches tomorrow at ${content.site} on ${content.date}")
            .setContentIntent(getLaunchDetailsPendingIntent(context, content.flightNumber))
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .build()
    }

    private fun getLaunchDetailsPendingIntent(context: Context, flightNumber: Int): PendingIntent {
        return NavDeepLinkBuilder(context)
            .setGraph(R.navigation.nav_graph)
            .setDestination(R.id.launchDetails)
            .setArguments(bundleOf("flightNumber" to flightNumber))
            .createPendingIntent()
    }

    override fun createChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                LaunchNotificationsManager.DAY_BEFORE_LAUNCH_CHANNEL_ID,
                context.getString(R.string.launchNotificationsDayBeforeLaunchChannelName),
                NotificationManager.IMPORTANCE_DEFAULT
            )

            NotificationManagerCompat
                .from(context)
                .createNotificationChannel(channel)
        }
    }
}

object WeekBeforeLaunch : LaunchNotification() {
    override fun create(context: Context, content: LaunchNotificationContent): Notification {

        createChannel(context)

        return NotificationCompat.Builder(
            context,
            LaunchNotificationsManager.WEEK_BEFORE_LAUNCH_CHANNEL_ID
        )
            .setSmallIcon(R.drawable.ic_round_rocket_small)
            .setLargeIcon(getMissionPatch(content.missionPatch, GlideApp.with(context)))
            .setContentTitle("${content.name} Launch")
            .setContentText("Launches this week")
            .setContentIntent(getLaunchDetailsPendingIntent(context, content.flightNumber))
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setGroup(LaunchNotificationsManager.WEEK_BEFORE_LAUNCH_GROUP_ID)
            .build()
    }

    private fun getLaunchDetailsPendingIntent(context: Context, flightNumber: Int): PendingIntent {
        return NavDeepLinkBuilder(context)
            .setGraph(R.navigation.nav_graph)
            .setDestination(R.id.launchDetails)
            .setArguments(bundleOf("flightNumber" to flightNumber))
            .createPendingIntent()
    }

    override fun createChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                LaunchNotificationsManager.WEEK_BEFORE_LAUNCH_CHANNEL_ID,
                context.getString(R.string.launchNotificationsWeekBeforeLaunchChannelName),
                NotificationManager.IMPORTANCE_DEFAULT
            )

            NotificationManagerCompat
                .from(context)
                .createNotificationChannel(channel)
        }
    }
}