package com.haroldadmin.moonshot.database.landing_pad

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.haroldadmin.moonshot.database.BaseDbTest
import com.haroldadmin.moonshot.models.landingpad.LandingPad
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
internal class LandingPadDaoTest : BaseDbTest() {

    private val dao by lazy { db.landingPadDao() }

    @Test
    fun landingPadWriteTest() = runBlocking {
        val landingPad = LandingPad.getSampleLandingPad()
        dao.saveLandingPad(landingPad)

        val savedLandingPad = dao.getLandingPad(landingPad.id)
        assertEquals(landingPad, savedLandingPad)
    }
}