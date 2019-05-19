package com.haroldadmin.moonshot_repository

import com.haroldadmin.moonshot.database.launch.LaunchDao
import com.haroldadmin.moonshot.database.launch.rocket.RocketSummaryDao
import com.haroldadmin.moonshot.database.launch.rocket.first_stage.FirstStageSummaryDao
import com.haroldadmin.moonshot.database.launch.rocket.second_stage.SecondStageSummaryDao
import com.haroldadmin.spacex_api_wrapper.capsule.CapsuleService
import com.haroldadmin.spacex_api_wrapper.core.CoresService
import com.haroldadmin.spacex_api_wrapper.dragon.DragonsService
import com.haroldadmin.spacex_api_wrapper.history.HistoryService
import com.haroldadmin.spacex_api_wrapper.info.InfoService
import com.haroldadmin.spacex_api_wrapper.landingpads.LandingPadsService
import com.haroldadmin.spacex_api_wrapper.launches.LaunchesService
import com.haroldadmin.spacex_api_wrapper.launchpad.LaunchPadService
import com.haroldadmin.spacex_api_wrapper.mission.MissionService
import com.haroldadmin.spacex_api_wrapper.payload.PayloadsService
import com.haroldadmin.spacex_api_wrapper.rocket.RocketsService
import org.koin.dsl.module

val repositoryModule = module {

    single<RemoteDataSource> {
        RemoteDataSource(
            get<CapsuleService>(),
            get<CoresService>(),
            get<DragonsService>(),
            get<HistoryService>(),
            get<InfoService>(),
            get<LandingPadsService>(),
            get<LaunchesService>(),
            get<LaunchPadService>(),
            get<MissionService>(),
            get<PayloadsService>(),
            get<RocketsService>()
        )
    }

    single<LocalDataSource> {
        LocalDataSource(
            get<FirstStageSummaryDao>(),
            get<SecondStageSummaryDao>(),
            get<RocketSummaryDao>(),
            get<LaunchDao>()
        )
    }

}