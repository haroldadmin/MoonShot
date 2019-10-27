package com.haroldadmin.moonshotRepository

import com.haroldadmin.moonshot.database.ApplicationInfoDao
import com.haroldadmin.moonshot.database.databaseModule
import com.haroldadmin.moonshot.database.LaunchDao
import com.haroldadmin.moonshot.database.LaunchPadDao
import com.haroldadmin.moonshot.database.RocketsDao
import com.haroldadmin.moonshotRepository.applicationInfo.ApplicationInfoUseCase
import com.haroldadmin.moonshotRepository.launch.GetLaunchDetailsUseCase
import com.haroldadmin.moonshotRepository.launch.GetLaunchPicturesUseCase
import com.haroldadmin.moonshotRepository.launch.GetLaunchesForLaunchpadUseCase
import com.haroldadmin.moonshotRepository.launch.GetLaunchesUseCase
import com.haroldadmin.moonshotRepository.launch.GetNextLaunchUseCase
import com.haroldadmin.moonshotRepository.launch.PersistLaunchesUseCase
import com.haroldadmin.moonshotRepository.launchPad.GetLaunchPadUseCase
import com.haroldadmin.moonshotRepository.launchPad.PersistLaunchPadUseCase
import com.haroldadmin.moonshotRepository.rocket.GetAllRocketsUseCase
import com.haroldadmin.moonshotRepository.rocket.GetLaunchesForRocketUseCase
import com.haroldadmin.moonshotRepository.rocket.GetRocketDetailsUseCase
import com.haroldadmin.moonshotRepository.rocket.PersistRocketsUseCase
import com.haroldadmin.moonshotRepository.search.SearchLaunchesUseCase
import com.haroldadmin.moonshotRepository.search.SearchLaunchpadsUseCase
import com.haroldadmin.moonshotRepository.search.SearchRocketsUseCase
import com.haroldadmin.spacex_api_wrapper.launches.LaunchesService
import com.haroldadmin.spacex_api_wrapper.launchpad.LaunchPadService
import com.haroldadmin.spacex_api_wrapper.networkModule
import com.haroldadmin.spacex_api_wrapper.rocket.RocketsService
import com.haroldadmin.spacex_api_wrapper.serviceModule
import okhttp3.Cache
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = databaseModule + networkModule + serviceModule + module {

    factory { PersistLaunchesUseCase(get<LaunchDao>()) }
    factory { GetLaunchesUseCase(get<LaunchDao>(), get<LaunchesService>(), get<PersistLaunchesUseCase>()) }
    factory { GetLaunchesForLaunchpadUseCase(get<LaunchDao>(), get<LaunchesService>(), get<PersistLaunchesUseCase>()) }
    factory { GetNextLaunchUseCase(get<LaunchDao>(), get<LaunchesService>(), get<PersistLaunchesUseCase>()) }
    factory { GetLaunchDetailsUseCase(get<LaunchDao>(), get<LaunchesService>(), get<PersistLaunchesUseCase>()) }
    factory { GetLaunchPicturesUseCase(get<LaunchDao>()) }

    factory { PersistLaunchPadUseCase(get<LaunchPadDao>()) }
    factory { GetLaunchPadUseCase(get<LaunchPadDao>(), get<LaunchPadService>(), get<PersistLaunchPadUseCase>()) }

    factory { PersistRocketsUseCase(get<RocketsDao>()) }
    factory { GetAllRocketsUseCase(get<RocketsDao>(), get<RocketsService>(), get<PersistRocketsUseCase>()) }
    factory { GetRocketDetailsUseCase(get<RocketsDao>(), get<RocketsService>(), get<PersistRocketsUseCase>()) }
    factory { GetLaunchesForRocketUseCase(get<RocketsDao>(), get<PersistLaunchesUseCase>(), get<LaunchesService>()) }

    factory { SearchLaunchesUseCase(get<LaunchDao>(), get<LaunchesService>(), get<PersistLaunchesUseCase>()) }
    factory { SearchLaunchpadsUseCase(get<LaunchPadDao>(), get<LaunchPadService>(), get<PersistLaunchPadUseCase>()) }
    factory { SearchRocketsUseCase(get<RocketsDao>(), get<RocketsService>(), get<PersistRocketsUseCase>()) }

    factory { ApplicationInfoUseCase(get<ApplicationInfoDao>()) }

    factory {
        SyncResourcesUseCase(
            get<GetLaunchesUseCase>(),
            get<PersistLaunchesUseCase>(),
            get<GetAllRocketsUseCase>(),
            get<PersistRocketsUseCase>(),
            get<GetLaunchPadUseCase>(),
            get<PersistLaunchPadUseCase>(),
            get<ApplicationInfoUseCase>()
        )
    }

    single<Cache> {
        Cache(androidContext().cacheDir, 10 * 1000 * 1000)
    }
}