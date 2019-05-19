package com.haroldadmin.moonshot

import com.haroldadmin.moonshot.base.MoonShotViewModel
import com.haroldadmin.moonshot_repository.LaunchesRepository

class MyVm (initialState: MyState,
            private val repository: LaunchesRepository): MoonShotViewModel<MyState>(initialState) {

    suspend fun getAllLaunches() {
        val launchList = repository.getAllLaunches()
        setState {
            copy(launches = launchList)
        }
    }

}