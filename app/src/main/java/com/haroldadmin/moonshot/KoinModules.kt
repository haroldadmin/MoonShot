package com.haroldadmin.moonshot

import android.os.Handler
import android.os.HandlerThread
import com.haroldadmin.moonshot.launchDetails.LaunchDetailsState
import com.haroldadmin.moonshot.launchDetails.LaunchDetailsViewModel
import com.haroldadmin.moonshot.launchPad.LaunchPadState
import com.haroldadmin.moonshot.launchPad.LaunchPadViewModel
import com.haroldadmin.moonshot.launches.LaunchesState
import com.haroldadmin.moonshot.launches.LaunchesViewModel
import com.haroldadmin.moonshot.nextLaunch.NextLaunchState
import com.haroldadmin.moonshot.nextLaunch.NextLaunchViewModel
import com.haroldadmin.moonshot.notifications.LaunchNotificationManager
import com.haroldadmin.moonshot.rocketDetails.RocketDetailsState
import com.haroldadmin.moonshot.rocketDetails.RocketDetailsViewModel
import com.haroldadmin.moonshot.sync.SyncManager
import com.haroldadmin.moonshotRepository.repositoryModule
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = repositoryModule + module {

    viewModel { (initialState: ScaffoldingState) -> MainViewModel(initialState) }

    viewModel { (initialState: LaunchesState) -> LaunchesViewModel(initialState, get()) }

    viewModel { (initialState: LaunchDetailsState) -> LaunchDetailsViewModel(initialState, get()) }

    viewModel { (initialState: NextLaunchState) -> NextLaunchViewModel(initialState, get(), get()) }

    viewModel { (initialState: LaunchPadState) -> LaunchPadViewModel(initialState, get()) }

    viewModel { (initialState: RocketDetailsState) -> RocketDetailsViewModel(initialState, get()) }

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

    factory { LaunchNotificationManager(androidContext()) }
}