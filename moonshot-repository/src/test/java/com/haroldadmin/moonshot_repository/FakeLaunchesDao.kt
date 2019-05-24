package com.haroldadmin.moonshot_repository

import com.haroldadmin.moonshot.database.launch.LaunchDao
import com.haroldadmin.moonshot.models.launch.Launch
import com.haroldadmin.moonshot.models.launch.rocket.RocketSummary

class FakeLaunchesDao(val sampleData: List<Launch> = listOf()): LaunchDao {
    override suspend fun getAllLaunches(): List<Launch> = sampleData

    override suspend fun getLaunch(flightNumber: Int): Launch = Launch.getSampleLaunch()

    override suspend fun getUpcomingLaunches(timestamp: Long): List<Launch> = sampleData

    override suspend fun getPastLaunches(timestamp: Long): List<Launch> = sampleData

    override suspend fun getRocketForLaunch(flightNumber: Int): RocketSummary = RocketSummary.getSampleRocketSummary(1)

    override suspend fun saveLaunch(launch: Launch) = Unit

    override suspend fun saveLaunches(vararg launch: Launch) = Unit

    override suspend fun saveLaunches(launches: List<Launch>) = Unit

    override suspend fun deleteLaunch(launch: Launch) = Unit

}