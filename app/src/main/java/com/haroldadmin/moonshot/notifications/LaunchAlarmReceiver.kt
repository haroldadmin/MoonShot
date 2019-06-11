package com.haroldadmin.moonshot.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.haroldadmin.moonshot.utils.log
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
        withContext(Dispatchers.IO) {
            val notification = LaunchNotificationBuilder.create(context)

            NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification)

            cleanupSharedPrefs(context)
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
        }
    }
}
