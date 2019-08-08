package com.haroldadmin.moonshotRepository

import com.haroldadmin.moonshot.database.databaseModule
import com.haroldadmin.moonshot.database.launch.LaunchDao
import com.haroldadmin.moonshot.database.launch.rocket.RocketSummaryDao
import com.haroldadmin.moonshot.database.launch.rocket.firstStage.FirstStageSummaryDao
import com.haroldadmin.moonshot.database.launch.rocket.secondStage.SecondStageSummaryDao
import com.haroldadmin.moonshot.database.launchPad.LaunchPadDao
import com.haroldadmin.moonshot.database.rocket.RocketsDao
import com.haroldadmin.moonshotRepository.launch.GetLaunchesForLaunchpadUseCase
import com.haroldadmin.moonshotRepository.launch.GetLaunchesUseCase
import com.haroldadmin.moonshotRepository.launch.GetNextLaunchUseCase
import com.haroldadmin.moonshotRepository.launch.LaunchesRepository
import com.haroldadmin.moonshotRepository.launch.PersistLaunchesUseCase
import com.haroldadmin.moonshotRepository.launchPad.GetLaunchPadUseCase
import com.haroldadmin.moonshotRepository.launchPad.PersistLaunchPadUseCase
import com.haroldadmin.moonshotRepository.rocket.GetAllRocketsUseCase
import com.haroldadmin.moonshotRepository.rocket.GetLaunchesForRocketUseCase
import com.haroldadmin.moonshotRepository.rocket.GetRocketDetailsUseCase
import com.haroldadmin.moonshotRepository.rocket.PersistRocketsUseCase
import com.haroldadmin.moonshotRepository.rocket.RocketsRepository
import com.haroldadmin.spacex_api_wrapper.launches.LaunchesService
import com.haroldadmin.spacex_api_wrapper.launchpad.LaunchPadService
import com.haroldadmin.spacex_api_wrapper.networkModule
import com.haroldadmin.spacex_api_wrapper.rocket.RocketsService
import com.haroldadmin.spacex_api_wrapper.serviceModule
import okhttp3.Cache
import org.koin.android.ext.koin.androidContext
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

    factory { PersistLaunchesUseCase(get<LaunchDao>()) }
    factory { GetLaunchesUseCase(get<LaunchDao>(), get<LaunchesService>(), get<PersistLaunchesUseCase>()) }
    factory { GetLaunchesForLaunchpadUseCase(get<LaunchDao>(), get<LaunchesService>(), get<PersistLaunchesUseCase>()) }
    factory { GetNextLaunchUseCase(get<LaunchDao>(), get<LaunchesService>(), get<PersistLaunchesUseCase>()) }

    factory { PersistLaunchPadUseCase(get<LaunchPadDao>()) }
    factory { GetLaunchPadUseCase(get<LaunchPadDao>(), get<LaunchPadService>(), get<PersistLaunchPadUseCase>()) }

    factory { PersistRocketsUseCase(get<RocketsDao>()) }
    factory { GetAllRocketsUseCase(get<RocketsDao>(), get<RocketsService>(), get<PersistRocketsUseCase>()) }
    factory { GetRocketDetailsUseCase(get<RocketsDao>(), get<RocketsService>(), get<PersistRocketsUseCase>()) }
    factory { GetLaunchesForRocketUseCase(get<RocketsDao>(), get<PersistLaunchesUseCase>(), get<LaunchesService>()) }

    single<Cache> {
        Cache(androidContext().cacheDir, 10 * 1000 * 1000)
    }
}