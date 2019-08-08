package com.haroldadmin.moonshotRepository

import com.haroldadmin.moonshot.models.launch.LaunchMinimal
import com.haroldadmin.moonshot.models.rocket.RocketMinimal
import com.haroldadmin.spacex_api_wrapper.launches.Launch
import com.haroldadmin.spacex_api_wrapper.rocket.Rocket

object FakeDataProvider {

    fun getDbLaunches(number: Int): List<LaunchMinimal> {
        return FakeDbDataProvider.getLaunches(number)
    }

    fun getDbLaunch(): LaunchMinimal {
        return getDbLaunches(1).first()
    }

    fun getApiLaunches(number: Int): List<Launch> {
        return FakeApiDataProvider.getLaunches(number)
    }

    fun getApiLaunch(): Launch {
        return getApiLaunches(1).first()
    }

    fun getDbRockets(number: Int): List<RocketMinimal> {
        return FakeDbDataProvider.getRockets(number)
    }

    fun getDbRocket(): RocketMinimal = getDbRockets(1).first()

    fun getApiRockets(number: Int): List<Rocket> {
        return FakeApiDataProvider.getApiRockets(number)
    }

    fun getApiRocket(): Rocket = getApiRockets(1).first()
}