package com.haroldadmin.moonshot.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.haroldadmin.moonshot.MoonShot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LaunchAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (!PreferenceManager.getDefaultSharedPreferences(context).getBoolean(LaunchNotificationManager.KEY_LAUNCH_NOTIFICATIONS, true)) return
        deliverIntent(context)
    }

    private fun deliverIntent(context: Context) = GlobalScope.launch {
        try {
            val notification = withContext(Dispatchers.IO) { LaunchNotificationBuilder.create(context) }
            NotificationManagerCompat.from(context).notify(LaunchNotificationManager.NOTIFICATION_ID, notification)
            cleanupSharedPrefs(context)
        } catch (e: Exception) {
            Log.e("LaunchAlarmReceiver", "An error occurred while delivering intent: $e")
        }
    }

    private fun cleanupSharedPrefs(context: Context) {
        val preferences = context.getSharedPreferences(MoonShot.MOONSHOT_SHARED_PREFS, Context.MODE_PRIVATE)
        preferences.edit {
            remove(LaunchNotificationManager.KEY_LAUNCH_NAME)
            remove(LaunchNotificationManager.KEY_LAUNCH_SITE)
            remove(LaunchNotificationManager.KEY_LAUNCH_DATE)
            remove(LaunchNotificationManager.KEY_LAUNCH_TIME)
            remove(LaunchNotificationManager.KEY_LAUNCH_MISSION_PATCH)
            remove(LaunchNotificationManager.KEY_FLIGHT_NUMBER)
        }
    }
}
