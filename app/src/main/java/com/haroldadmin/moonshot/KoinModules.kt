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
import com.haroldadmin.moonshot.rockets.RocketsState
import com.haroldadmin.moonshot.rockets.RocketsViewModel
import com.haroldadmin.moonshotRepository.repositoryModule
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = repositoryModule + module {
    viewModel { (initialState: LaunchesState) -> LaunchesViewModel(initialState, get()) }

    viewModel { (initialState: RocketsState) -> RocketsViewModel(initialState, get()) }

    viewModel { (initialState: LaunchDetailsState) -> LaunchDetailsViewModel(initialState, get()) }

    viewModel { (initialState: NextLaunchState) -> NextLaunchViewModel(initialState, get()) }

    viewModel { (initialState: LaunchPadState) -> LaunchPadViewModel(initialState, get()) }

    single(named("diffing-thread")) {
        HandlerThread("epoxy-diffing-thread").apply { start() }
    }
    single(named("building-thread")) {
        HandlerThread("epoxy-model-building-thread").apply { start() }
    }

    single(named("differ")) { Handler(get<HandlerThread>(named("diffing-thread")).looper) }
    single(named("builder")) { Handler(get<HandlerThread>(named("building-thread")).looper) }
}