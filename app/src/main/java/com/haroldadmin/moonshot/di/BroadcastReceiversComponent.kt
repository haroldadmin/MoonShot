package com.haroldadmin.moonshot.di

import com.haroldadmin.moonshot.base.di.AppScope
import com.haroldadmin.moonshot.notifications.receivers.BootCompleteReceiver
import com.haroldadmin.moonshot.notifications.receivers.DayBeforeLaunchAlarmReceiver
import com.haroldadmin.moonshot.notifications.receivers.JustBeforeLaunchAlarmReceiver
import com.haroldadmin.moonshot.notifications.receivers.TimeChangedReceiver
import dagger.Subcomponent

@Subcomponent
@AppScope
interface BroadcastReceiversComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): BroadcastReceiversComponent
    }
    fun inject(receiver: BootCompleteReceiver)
    fun inject(receiver: DayBeforeLaunchAlarmReceiver)
    fun inject(receiver: JustBeforeLaunchAlarmReceiver)
    fun inject(receiver: TimeChangedReceiver)
}