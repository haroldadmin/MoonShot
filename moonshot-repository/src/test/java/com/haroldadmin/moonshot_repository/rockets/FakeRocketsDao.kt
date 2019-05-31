package com.haroldadmin.moonshot_repository.rockets

import com.haroldadmin.moonshot.database.rocket.RocketsDao
import com.haroldadmin.moonshot.models.rocket.PayloadWeight
import com.haroldadmin.moonshot.models.rocket.RocketWithPayloadWeights
import com.haroldadmin.moonshot.models.rocket.Rocket as DbRocket
import com.haroldadmin.moonshot.models.rocket.PayloadWeight as DbPayloadWeight

class FakeRocketsDao(
    private val sampleRockets: List<DbRocket> = listOf(),
    private val samplePayloadWeights: List<DbPayloadWeight> = listOf()
) : RocketsDao() {
    override suspend fun save(obj: DbRocket) = Unit

    override suspend fun saveAll(vararg obj: DbRocket) = Unit

    override suspend fun saveAll(objs: List<DbRocket>) = Unit

    override suspend fun update(obj: DbRocket) = Unit

    override suspend fun updateAll(vararg obj: DbRocket) = Unit

    override suspend fun updateAll(objs: List<DbRocket>) = Unit

    override suspend fun delete(obj: DbRocket) = Unit

    override suspend fun deleteAll(vararg obj: DbRocket) = Unit

    override suspend fun deleteAll(objs: List<DbRocket>) = Unit

    override suspend fun getAllRockets(): List<DbRocket> = sampleRockets

    override suspend fun getRocket(rocketId: String): DbRocket = DbRocket.getSampleRocket()

    override suspend fun getRocketWithPayloadWeights(rocketId: String): RocketWithPayloadWeights {
        return RocketWithPayloadWeights(
            DbRocket.getSampleRocket(),
            samplePayloadWeights
        )
    }

    override suspend fun savePayloadWeights(payloadWeights: List<PayloadWeight>) = Unit

    override suspend fun deleteRocket(rocket: DbRocket) = Unit
}
