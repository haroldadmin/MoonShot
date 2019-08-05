package com.haroldadmin.moonshot.nextLaunch

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val nextLaunchModule = module {
    viewModel { (initialState: NextLaunchState) -> NextLaunchViewModel(initialState, get()) }
}