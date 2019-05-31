package com.haroldadmin.moonshot_repository

import com.haroldadmin.moonshot.database.databaseModule
import com.haroldadmin.moonshot.database.launch.LaunchDao
import com.haroldadmin.moonshot.database.launch.rocket.RocketSummaryDao
import com.haroldadmin.moonshot.database.launch.rocket.first_stage.FirstStageSummaryDao
import com.haroldadmin.moonshot.database.launch.rocket.second_stage.SecondStageSummaryDao
import com.haroldadmin.moonshot.database.rocket.RocketsDao
import com.haroldadmin.moonshot_repository.launch.LaunchesRepository
import com.haroldadmin.moonshot_repository.rocket.RocketsRepository
import com.haroldadmin.spacex_api_wrapper.launches.LaunchesService
import com.haroldadmin.spacex_api_wrapper.networkModule
import com.haroldadmin.spacex_api_wrapper.rocket.RocketsService
import com.haroldadmin.spacex_api_wrapper.serviceModule
import org.koin.dsl.module

val repositoryModule = databaseModule + networkModule + serviceModule + module {

    single<LaunchesRepository> {
        LaunchesRepository(
            get<LaunchDao>(),
            get<RocketSummaryDao>(),
            get<FirstStageSummaryDao>(),
            get<SecondStageSummaryDao>(),
            get<LaunchesService>()
        )
    }

    single<RocketsRepository> {
        RocketsRepository(
            get<RocketsService>(),
            get<RocketsDao>()
        )
    }
}