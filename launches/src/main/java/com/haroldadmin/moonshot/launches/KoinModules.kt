package com.haroldadmin.moonshot.launches

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val launchesModule = module {
    viewModel { (initialState: LaunchesState) ->
        LaunchesViewModel(
            initialState,
            get()
        )
    }
}