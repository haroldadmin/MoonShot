package com.haroldadmin.moonshotRepository.launches

import com.haroldadmin.moonshot.database.launch.LaunchDao
import com.haroldadmin.moonshot.models.launch.Launch
import com.haroldadmin.moonshot.models.launch.LaunchMinimal
import com.haroldadmin.moonshot.models.launch.LaunchPictures
import com.haroldadmin.moonshot.models.launch.LaunchStats
import com.haroldadmin.moonshot.models.launch.rocket.RocketSummary
import com.haroldadmin.moonshot.models.launch.rocket.firstStage.CoreSummary
import com.haroldadmin.moonshot.models.launch.rocket.firstStage.FirstStageSummary
import com.haroldadmin.moonshot.models.launch.rocket.secondStage.SecondStageSummary
import com.haroldadmin.moonshot.models.launch.rocket.secondStage.payload.Payload

class FakeLaunchesDao : LaunchDao() {

    override suspend fun getPastLaunchesForLaunchPad(
        siteId: String,
        maxTimeStamp: Long,
        limit: Int
    ): List<LaunchMinimal> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getUpcomingLaunchesForLaunchPad(
        siteId: String,
        minTimeStamp: Long,
        limit: Int
    ): List<LaunchMinimal> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getLaunchesInRange(start: Long, end: Long, maxCount: Int): List<Launch> {
        return listOf()
    }

    override suspend fun getAllLaunchesForLaunchPad(
        siteId: String,
        limit: Int
    ): List<LaunchMinimal> {
        return listOf()
    }

    override suspend fun getNextLaunchMinimal(currentTime: Long): LaunchMinimal? = null

    override suspend fun getLaunchStats(flightNumber: Int): LaunchStats? = null

    override suspend fun getLaunchMinimal(flightNumber: Int): LaunchMinimal? = null

    override suspend fun getLaunchPictures(flightNumber: Int): LaunchPictures? = null

    override suspend fun saveLaunch(launches: Launch) = Unit

    override suspend fun saveLaunches(launches: List<Launch>) = Unit

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

    override suspend fun getNextLaunch(timestamp: Long): Launch = Launch.getSampleLaunch()

    override suspend fun getUpcomingLaunches(timestamp: Long, limit: Int): List<LaunchMinimal> = listOf()

    override suspend fun getPastLaunches(timestamp: Long, limit: Int): List<LaunchMinimal> = listOf()

    override suspend fun getAllLaunches(limit: Int): List<LaunchMinimal> = listOf()
}