package com.haroldadmin.moonshot.database.launchPad

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.haroldadmin.moonshot.database.BaseDbTest
import com.haroldadmin.moonshot.models.launchpad.LaunchPad
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
internal class LaunchPadDaoTest: BaseDbTest() {

    private val dao by lazy { db.launchpadDao() }

    @Test
    fun readAndWriteTest() = runBlocking {
        val launchPad = LaunchPad.getSampleLanchpad()
        dao.save(launchPad)

        val savedLaunchpad = dao.getLaunchPad(launchPad.site_id)

        assertEquals(launchPad, savedLaunchpad)
    }

    @Test
    fun deleteTest() = runBlocking {
        val launchpad = LaunchPad.getSampleLanchpad()
        dao.save(launchpad)
        dao.delete(launchpad)

        assertTrue(dao.getAllLaunchPads(limit = Int.MAX_VALUE).isEmpty())
    }

    @Test
    fun limitTest() = runBlocking {
        dao.save(LaunchPad.getSampleLanchpad())
        val launchpads = dao.getAllLaunchPads(limit = 0)
        assertTrue(launchpads.isEmpty())
    }
}