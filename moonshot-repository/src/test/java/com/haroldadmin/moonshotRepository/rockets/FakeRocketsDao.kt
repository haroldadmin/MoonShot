package com.haroldadmin.moonshotRepository.rockets

import com.haroldadmin.moonshot.database.RocketsDao
import com.haroldadmin.moonshot.models.Rocket
import com.haroldadmin.moonshot.models.SampleDbData
import com.haroldadmin.moonshot.models.launch.Launch

internal class FakeRocketsDao : RocketsDao() {

    var didSynchronize: Boolean = false

    override suspend fun all(limit: Int, offset: Int): List<Rocket> {
        return SampleDbData.Rockets.many().take(limit).toList()
    }

    override suspend fun one(rocketId: String): Rocket? {
        return SampleDbData.Rockets.one(rocketId = rocketId)
    }

    override suspend fun launchesForRocket(rocketId: String, limit: Int, offset: Int): List<Launch> {
        return SampleDbData.Launches.many(rocketIdGenerator = { rocketId }).take(limit).toList()
    }

    override suspend fun forQuery(query: String, limit: Int, offset: Int): List<Rocket> {
        return SampleDbData.Rockets.many(rocketNameGenerator = { query }).take(limit).toList()
    }

    override suspend fun clearTable() = Unit

    override suspend fun save(obj: Rocket) = Unit

    override suspend fun saveAll(objs: List<Rocket>) = Unit

    override suspend fun update(obj: Rocket) = Unit

    override suspend fun updateAll(objs: List<Rocket>) = Unit

    override suspend fun delete(obj: Rocket) = Unit

    override suspend fun deleteAll(objs: List<Rocket>) = Unit

    override fun synchronizeBlocking(allRockets: List<Rocket>) {
        didSynchronize = true
    }
}