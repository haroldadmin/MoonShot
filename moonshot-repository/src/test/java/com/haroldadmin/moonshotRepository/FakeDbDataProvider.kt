package com.haroldadmin.moonshotRepository

import com.haroldadmin.moonshot.models.launch.Launch
import com.haroldadmin.moonshot.models.launch.LaunchMinimal
import com.haroldadmin.moonshot.models.launch.toLaunchMinimal
import com.haroldadmin.moonshot.models.rocket.Rocket
import com.haroldadmin.moonshot.models.rocket.RocketMinimal
import com.haroldadmin.moonshot.models.rocket.toRocketMinimal

object FakeDbDataProvider {
    fun getLaunches(number: Int): List<LaunchMinimal> {
        return generateSequence { Launch.getSampleLaunch() }
            .map { it.toLaunchMinimal() }
            .take(number)
            .toList()
    }

    fun getRockets(number: Int): List<RocketMinimal> {
        return generateSequence { Rocket.getSampleRocket() }
            .map { it.toRocketMinimal() }
            .take(number)
            .toList()
    }
}