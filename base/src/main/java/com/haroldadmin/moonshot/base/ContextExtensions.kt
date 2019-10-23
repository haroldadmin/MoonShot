package com.haroldadmin.moonshot.base

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.AlarmManagerCompat

inline fun <reified T> Context.intent(initializer: Intent.() -> Unit = {}): Intent {
    return Intent(this, T::class.java).apply(initializer)
}

inline fun Context.exactAlarmAt(
    time: Long,
    type: Int = AlarmManager.RTC_WAKEUP,
    alarmManager: AlarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager,
    crossinline pendingIntentProvider: Context.() -> PendingIntent
) {
    AlarmManagerCompat.setExactAndAllowWhileIdle(
        alarmManager,
        type,
        time,
        pendingIntentProvider()
    )
}

inline fun Context.broadcastPendingIntent(
    requestCode: Int,
    flags: Int = PendingIntent.FLAG_UPDATE_CURRENT,
    crossinline intentProvider: Context.() -> Intent
): PendingIntent {
    return PendingIntent.getBroadcast(this, requestCode, intentProvider(), flags)
}

inline fun Context.cancelAlarmWithIntent(
    alarmManager: AlarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager,
    crossinline pendingIntentProvider: Context.() -> PendingIntent
) {
    alarmManager.cancel(pendingIntentProvider())
}
