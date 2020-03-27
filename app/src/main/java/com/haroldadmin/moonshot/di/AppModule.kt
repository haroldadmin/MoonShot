package com.haroldadmin.moonshot.di

import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.os.Handler
import android.os.HandlerThread
import androidx.core.app.NotificationManagerCompat
import androidx.preference.PreferenceManager
import androidx.work.WorkManager
import com.haroldadmin.moonshot.notifications.RealSystemNotificationManager
import com.haroldadmin.moonshot.notifications.SystemNotificationManager
import com.squareup.inject.assisted.dagger2.AssistedModule
import dagger.Module
import dagger.Provides
import javax.inject.Named

@AssistedModule
@Module(includes = [AssistedInject_AppModule::class])
object AppModule {
    @Provides
    @Named("settings")
    fun settings(context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    @Provides
    @Named("epoxy-differ")
    fun epoxyDiffer(): Handler {
        val handlerThread = HandlerThread("epoxy-differ")
        handlerThread.start()
        return Handler(handlerThread.looper)
    }

    @Provides
    @Named("epoxy-builder")
    fun epoxyBuilder(): Handler {
        val handlerThread = HandlerThread("epoxy-differ")
        handlerThread.start()
        return Handler(handlerThread.looper)
    }

    @Provides
    fun workManager(context: Context): WorkManager {
        return WorkManager.getInstance(context)
    }

    @Provides
    fun notificationManager(context: Context): SystemNotificationManager {
        return RealSystemNotificationManager(NotificationManagerCompat.from(context))
    }
}
