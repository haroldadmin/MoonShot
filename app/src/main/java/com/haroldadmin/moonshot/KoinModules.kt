package com.haroldadmin.moonshot

import com.haroldadmin.moonshot.notifications.DayBeforeLaunch
import com.haroldadmin.moonshot.notifications.JustBeforeLaunch
import com.haroldadmin.moonshot.notifications.LaunchNotification
import com.haroldadmin.moonshot.notifications.LaunchNotificationsManager
import com.haroldadmin.moonshot.notifications.RealLaunchNotificationsManager
import com.haroldadmin.moonshot.sync.SyncManager
import com.haroldadmin.moonshotRepository.repositoryModule
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = repositoryModule + module {

    single { SyncManager(androidApplication()) }

    single<LaunchNotificationsManager> {
        RealLaunchNotificationsManager(
            androidContext()
        )
    }

    factory<LaunchNotification>(named("just-before-launch")) { JustBeforeLaunch }
    factory<LaunchNotification>(named("day-before-launch")) { DayBeforeLaunch }
}