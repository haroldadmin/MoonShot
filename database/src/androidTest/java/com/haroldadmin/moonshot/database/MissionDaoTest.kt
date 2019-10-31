package com.haroldadmin.moonshot.database

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.haroldadmin.moonshot.models.SampleDbData
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
internal class MissionDaoTest: DaoTest() {

    private val dao: MissionDao by dbRule.dao()

    @Test
    fun missionDetailsTest() = runBlocking {
        val testId = "F3364BF"
        val sample = SampleDbData.Missions.one(id = testId).also { dao.save(it) }
        val expected = sample
        val actual = dao.forId(testId)

        assertEquals(expected, actual)
    }
}