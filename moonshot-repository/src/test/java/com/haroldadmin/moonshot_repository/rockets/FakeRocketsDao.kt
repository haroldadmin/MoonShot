package com.haroldadmin.moonshot_repository.rockets

import com.haroldadmin.moonshot.database.rocket.RocketsDao
import com.haroldadmin.moonshot.models.rocket.PayloadWeight
import com.haroldadmin.moonshot.models.rocket.RocketWithPayloadWeights
import com.haroldadmin.moonshot.models.rocket.Rocket as DbRocket
import com.haroldadmin.moonshot.models.rocket.PayloadWeight as DbPayloadWeight

class FakeRocketsDao(
    val sampleRockets: List<DbRocket> = listOf(),
    val samplePayloadWeights: List<DbPayloadWeight> = listOf()
): RocketsDao {
    override suspend fun getAllRockets(): List<DbRocket> = sampleRockets

    override suspend fun getRocket(rocketId: String): DbRocket = DbRocket.getSampleRocket()

    override suspend fun getRocketWithPayloadWeights(rocketId: String): RocketWithPayloadWeights {
        return RocketWithPayloadWeights(
            DbRocket.getSampleRocket(),
            samplePayloadWeights
        )
    }

    override suspend fun saveRocket(rocket: DbRocket) = Unit

    override suspend fun saveRockets(vararg rocket: DbRocket) = Unit

    override suspend fun saveRockets(rockets: List<DbRocket>) = Unit

    override suspend fun savePayloadWeights(payloadWeights: List<PayloadWeight>) = Unit

    override suspend fun deleteRocket(rocket: DbRocket) = Unit


}
