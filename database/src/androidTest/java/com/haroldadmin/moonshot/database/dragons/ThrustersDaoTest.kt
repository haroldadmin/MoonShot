package com.haroldadmin.moonshot.database.dragons

import com.haroldadmin.moonshot.database.BaseDbTest
import com.haroldadmin.moonshot.models.dragon.Dragon
import com.haroldadmin.moonshot.models.dragon.Thruster
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

internal class ThrustersDaoTest : BaseDbTest() {

    private val thrustersDao by lazy { db.thrustersDao() }
    private val dragonDao by lazy { db.dragonsDao() }

    @Test
    fun thrusterWriteTest() = runBlocking {
        val dragon = Dragon.getSampleDragon()
        dragonDao.saveDragon(dragon)
        val tempThruster = Thruster.getSampleThruster(dragon.dragonId)
        thrustersDao.saveThruster(tempThruster)

        val thruster = thrustersDao.getAllThrusters().first()
        assertEquals(tempThruster, thruster)
    }
}