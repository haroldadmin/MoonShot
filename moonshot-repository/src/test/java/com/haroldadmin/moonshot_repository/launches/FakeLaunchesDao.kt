package com.haroldadmin.moonshot_repository.launches

import com.haroldadmin.moonshot.database.launch.LaunchDao
import com.haroldadmin.moonshot.models.launch.Launch
import com.haroldadmin.moonshot.models.launch.rocket.RocketSummary
import com.haroldadmin.moonshot.models.launch.rocket.first_stage.CoreSummary
import com.haroldadmin.moonshot.models.launch.rocket.first_stage.FirstStageSummary
import com.haroldadmin.moonshot.models.launch.rocket.second_stage.SecondStageSummary
import com.haroldadmin.moonshot.models.launch.rocket.second_stage.payload.Payload

class FakeLaunchesDao(val sampleData: List<Launch> = listOf()) : LaunchDao() {

    override suspend fun saveRocketSummary(rocketSummary: RocketSummary) = Unit

    override suspend fun saveRocketSummaries(rocketSummaries: List<RocketSummary>) = Unit

    override suspend fun saveFirstStageSummary(firstStageSummary: FirstStageSummary) = Unit

    override suspend fun saveFirstStageSummaries(firstStageSummaries: List<FirstStageSummary>) = Unit

    override suspend fun saveSecondStageSummary(secondStageSummary: SecondStageSummary) = Unit

    override suspend fun saveSecondStageSummaries(secondStageSummaries: List<SecondStageSummary>) = Unit

    override suspend fun saveCoreSummaries(coreSummaries: List<CoreSummary>) = Unit

    override suspend fun savePayloads(payloads: List<Payload>) = Unit

    override suspend fun save(obj: Launch) = Unit

    override suspend fun saveAll(vararg obj: Launch) = Unit

    override suspend fun saveAll(objs: List<Launch>) = Unit

    override suspend fun update(obj: Launch) = Unit

    override suspend fun updateAll(vararg obj: Launch) = Unit

    override suspend fun updateAll(objs: List<Launch>) = Unit

    override suspend fun delete(obj: Launch) = Unit

    override suspend fun deleteAll(vararg obj: Launch) = Unit

    override suspend fun deleteAll(objs: List<Launch>) = Unit

    override suspend fun getAllLaunches(): List<Launch> = sampleData

    override suspend fun getLaunch(flightNumber: Int): Launch = Launch.getSampleLaunch()

    override suspend fun getUpcomingLaunches(timestamp: Long): List<Launch> = sampleData

    override suspend fun getPastLaunches(timestamp: Long): List<Launch> = sampleData

    override suspend fun getRocketForLaunch(flightNumber: Int): RocketSummary = RocketSummary.getSampleRocketSummary(1)

    override suspend fun getNextLaunch(timestamp: Long): Launch = Launch.getSampleLaunch()
}