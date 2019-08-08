package com.haroldadmin.moonshot.launchDetails

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

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