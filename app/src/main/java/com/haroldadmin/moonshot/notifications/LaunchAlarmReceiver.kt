package com.haroldadmin.moonshot.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LaunchAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (!PreferenceManager.getDefaultSharedPreferences(context).getBoolean(KEY_LAUNCH_NOTIFICATIONS, true)) return
        deliverIntent(context)
    }

    private fun deliverIntent(context: Context) = GlobalScope.launch {
        try {
            val notification = withContext(Dispatchers.IO) { LaunchNotificationBuilder.create(context) }
            NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification)
            cleanupSharedPrefs(context)
        } catch (e: Exception) {
            Log.e("LaunchAlarmReceiver", "An error occurred while delivering intent: $e")
        }
    }

    private fun cleanupSharedPrefs(context: Context) {
        val preferences = context.getSharedPreferences(MOONSHOT_SHARED_PREFS, Context.MODE_PRIVATE)
        preferences.edit {
            remove(KEY_LAUNCH_NAME)
            remove(KEY_LAUNCH_SITE)
            remove(KEY_LAUNCH_DATE)
            remove(KEY_LAUNCH_TIME)
            remove(KEY_LAUNCH_MISSION_PATCH)
            remove(KEY_FLIGHT_NUMBER)
        }
    }
}
