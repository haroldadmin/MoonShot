package com.haroldadmin.moonshot.launchDetails

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

@ExperimentalCoroutinesApi
val launchDetailsModule = module {
    viewModel { (initialState: LaunchDetailsState) ->
        LaunchDetailsViewModel(
            initState = initialState,
            launchDetailsUseCase = get(),
            launchStatsUseCase = get(),
            launchPicturesUseCase = get()
        )
    }
}