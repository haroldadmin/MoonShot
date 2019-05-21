package com.haroldadmin.moonshot.database.dragons

import com.haroldadmin.moonshot.database.BaseDbTest
import com.haroldadmin.moonshot.models.dragon.Dragon
import com.haroldadmin.moonshot.models.dragon.Thruster
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

internal class DragonsDaoTest: BaseDbTest() {

    private val dragonsDao by lazy { db.dragonsDao() }
    private val thrustersDao by lazy { db.thrustersDao() }

    @Test
    fun dragonWriteTest() = runBlocking {
        val dragon = Dragon.getSampleDragon()
        val thruster = Thruster.getSampleThruster(dragon.dragonId)
        dragonsDao.saveDragon(dragon)
        thrustersDao.saveThruster(thruster)
        val savedDragon = dragonsDao.getDragon(dragon.dragonId)
        val savedDragonWithThrusters = dragonsDao.getDragonWithThrusters(dragon.dragonId)

        assertEquals(dragon, savedDragon)
        assertEquals(1, savedDragonWithThrusters.thrusters.size)
    }
}