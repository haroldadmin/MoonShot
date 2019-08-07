package com.haroldadmin.moonshot.launchPad

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val launchPadModule = module {
    viewModel { (initialState: LaunchPadState) -> LaunchPadViewModel(initialState, get()) }
}