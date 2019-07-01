package com.haroldadmin.moonshot

import android.os.Handler
import android.os.HandlerThread
import com.haroldadmin.moonshot.notifications.LaunchNotificationBuilder
import com.haroldadmin.moonshot.notifications.LaunchNotificationManagerImpl
import com.haroldadmin.moonshot.notifications.LaunchNotificationsManager
import com.haroldadmin.moonshot.sync.SyncManager
import com.haroldadmin.moonshotRepository.repositoryModule
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = repositoryModule + module {

    viewModel { (initialState: ScaffoldingState) -> MainViewModel(initialState) }

    single(named("diffing-thread")) {
        HandlerThread("epoxy-diffing-thread").apply { start() }
    }
    single(named("building-thread")) {
        HandlerThread("epoxy-model-building-thread").apply { start() }
    }

    single(named("differ")) { Handler(get<HandlerThread>(named("diffing-thread")).looper) }
    single(named("builder")) { Handler(get<HandlerThread>(named("building-thread")).looper) }

    single { MoonShotWorkerFactory(getKoin()) }
    single { SyncManager(androidApplication()) }

    single<LaunchNotificationsManager> { LaunchNotificationManagerImpl(androidContext()) }
    factory<LaunchNotificationBuilder> { LaunchNotificationBuilder() }
}