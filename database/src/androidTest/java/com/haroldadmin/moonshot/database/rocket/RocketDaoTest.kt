package com.haroldadmin.moonshot.database.rocket

import com.haroldadmin.moonshot.database.BaseDbTest
import com.haroldadmin.moonshot.models.rocket.PayloadWeight
import com.haroldadmin.moonshot.models.rocket.Rocket
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

internal class RocketDaoTest : BaseDbTest() {

    private val payloadWeightsDao by lazy { db.payloadWeightsDao() }
    private val dao by lazy { db.rocketsDao() }

    @Test
    fun rocketReadWriteTest() = runBlocking {
        val rocket = Rocket.getSampleRocket()
        dao.save(rocket)
        val savedRocket = dao.getRocket(rocket.rocketId)

        assertEquals(rocket, savedRocket)
    }

    @Test
    fun rocketWithPayloadWeightsTest() = runBlocking {
        val rocket = Rocket.getSampleRocket()
        dao.save(rocket)

        val payloadWeights = arrayOf(PayloadWeight.gameSamplePayloadWeight(rocket.rocketId))
        payloadWeightsDao.saveAll(*payloadWeights)

        val savedRocketWithPayloadWeights = dao.getRocketWithPayloadWeights(rocket.rocketId)
        assertEquals(payloadWeights.toList(), savedRocketWithPayloadWeights.payloadWeights)
    }
}