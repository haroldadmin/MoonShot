package com.haroldadmin.moonshot

import com.haroldadmin.moonshot.notifications.DayBeforeLaunch
import com.haroldadmin.moonshot.notifications.JustBeforeLaunch
import com.haroldadmin.moonshot.notifications.LaunchNotification
import com.haroldadmin.moonshot.notifications.LaunchNotificationManagerImpl
import com.haroldadmin.moonshot.notifications.LaunchNotificationsManager
import com.haroldadmin.moonshot.notifications.WeekBeforeLaunch
import com.haroldadmin.moonshot.sync.SyncManager
import com.haroldadmin.moonshotRepository.repositoryModule
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = repositoryModule + module {

    single { SyncManager(androidApplication()) }

    single<LaunchNotificationsManager> { LaunchNotificationManagerImpl(androidContext()) }

    factory<LaunchNotification>(named("just-before-launch")) { JustBeforeLaunch }
    factory<LaunchNotification>(named("day-before-launch")) { DayBeforeLaunch }
    factory<LaunchNotification>(named("week-before-launch")) { WeekBeforeLaunch }
}