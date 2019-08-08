package com.haroldadmin.moonshotRepository.rockets

import com.haroldadmin.moonshot.database.rocket.RocketsDao
import com.haroldadmin.moonshot.models.launch.LaunchMinimal
import com.haroldadmin.moonshot.models.rocket.PayloadWeight
import com.haroldadmin.moonshot.models.rocket.RocketMinimal
import com.haroldadmin.moonshot.models.rocket.RocketWithPayloadWeights
import com.haroldadmin.moonshot.models.rocket.toRocketMinimal
import com.haroldadmin.moonshot.models.rocket.Rocket as DbRocket
import com.haroldadmin.moonshot.models.rocket.PayloadWeight as DbPayloadWeight

class FakeRocketsDao(
    private val samplePayloadWeights: List<DbPayloadWeight> = listOf()
) : RocketsDao() {
    override suspend fun getAllRockets(): List<RocketMinimal> = listOf()

    override suspend fun getLaunchesForRocket(
        rocketId: String,
        timestamp: Long,
        limit: Int
    ): List<LaunchMinimal> = listOf()

    override suspend fun save(obj: DbRocket) = Unit

    override suspend fun saveAll(vararg obj: DbRocket) = Unit

    override suspend fun saveAll(objs: List<DbRocket>) = Unit

    override suspend fun update(obj: DbRocket) = Unit

    override suspend fun updateAll(vararg obj: DbRocket) = Unit

    override suspend fun updateAll(objs: List<DbRocket>) = Unit

    override suspend fun delete(obj: DbRocket) = Unit

    override suspend fun deleteAll(vararg obj: DbRocket) = Unit

    override suspend fun deleteAll(objs: List<DbRocket>) = Unit

    override suspend fun getRocket(rocketId: String): RocketMinimal? =
        DbRocket.getSampleRocket().toRocketMinimal()

    override suspend fun getRocketWithPayloadWeights(rocketId: String): RocketWithPayloadWeights {
        return RocketWithPayloadWeights(
            DbRocket.getSampleRocket(),
            samplePayloadWeights
        )
    }

    override suspend fun savePayloadWeights(payloadWeights: List<PayloadWeight>) = Unit

    override suspend fun deleteRocket(rocket: DbRocket) = Unit
}
