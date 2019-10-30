package com.haroldadmin.moonshot.di

import com.haroldadmin.moonshot.notifications.DayBeforeLaunch
import com.haroldadmin.moonshot.notifications.JustBeforeLaunch
import com.haroldadmin.moonshot.notifications.LaunchNotification
import com.haroldadmin.moonshot.notifications.LaunchNotificationsManager
import com.haroldadmin.moonshot.notifications.RealLaunchNotificationsManager
import dagger.Binds
import dagger.Module
import javax.inject.Named

@Module(subcomponents = [BroadcastReceiversComponent::class])
interface NotificationModule {
    @Binds
    @Named("just-before-launch")
    fun justBeforeLaunchNotification(notification: JustBeforeLaunch): LaunchNotification

    @Binds
    @Named("day-before-launch")
    fun dayBeforeLaunchNotification(notification: DayBeforeLaunch): LaunchNotification

    @Binds
    fun launchNotificationsManager(realLaunchNotificationsManager: RealLaunchNotificationsManager): LaunchNotificationsManager
}