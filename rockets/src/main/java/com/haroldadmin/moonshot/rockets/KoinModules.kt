package com.haroldadmin.moonshot.rockets

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val rocketsModule = module {

    viewModel { (initialState: RocketsState) -> RocketsViewModel(
        initState = initialState,
        allRocketsUseCase = get()
    ) }
}