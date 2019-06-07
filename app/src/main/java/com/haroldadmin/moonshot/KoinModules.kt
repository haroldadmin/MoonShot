package com.haroldadmin.moonshot

import com.haroldadmin.moonshot.launchDetails.LaunchDetailsState
import com.haroldadmin.moonshot.launchDetails.LaunchDetailsViewModel
import com.haroldadmin.moonshot.launches.LaunchesState
import com.haroldadmin.moonshot.launches.LaunchesViewModel
import com.haroldadmin.moonshot.rockets.RocketsState
import com.haroldadmin.moonshot.rockets.RocketsViewModel
import com.haroldadmin.moonshotRepository.repositoryModule
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = repositoryModule + module {
    viewModel { (initialState: LaunchesState) -> LaunchesViewModel(initialState, get()) }

    viewModel { (initialState: RocketsState) -> RocketsViewModel(initialState, get()) }

    viewModel { (initialState: LaunchDetailsState) -> LaunchDetailsViewModel(initialState, get()) }
}