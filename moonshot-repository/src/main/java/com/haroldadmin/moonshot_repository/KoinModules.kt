package com.haroldadmin.moonshot_repository

import com.haroldadmin.moonshot.database.databaseModule
import com.haroldadmin.moonshot.database.launch.LaunchDao
import com.haroldadmin.moonshot_repository.launch.LaunchesRepository
import com.haroldadmin.spacex_api_wrapper.launches.LaunchesService
import com.haroldadmin.spacex_api_wrapper.networkModule
import com.haroldadmin.spacex_api_wrapper.serviceModule
import org.koin.dsl.module

val repositoryModule = databaseModule + networkModule + serviceModule + module {

    single<LaunchesRepository> {
        LaunchesRepository(
            get<LaunchDao>(),
            get<LaunchesService>()
        )
    }
}