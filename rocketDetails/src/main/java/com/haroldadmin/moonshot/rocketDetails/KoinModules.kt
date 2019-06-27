package com.haroldadmin.moonshot.rocketDetails

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val rocketDetailsModule = module {

    viewModel { (initialState: RocketDetailsState) ->
        RocketDetailsViewModel(
            initialState,
            get()
        )
    }
}
