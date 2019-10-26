package com.haroldadmin.moonshot.database

import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import com.haroldadmin.moonshot.models.SampleDbData
import com.haroldadmin.moonshot.models.SearchQuery
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
internal class LaunchPadDaoTest: DaoTest() {

    private val dao: LaunchPadDao by dbRule.dao()

    @Test
    fun launchPadDetailsTest() = runBlocking {
        val sample = SampleDbData.LaunchPads.one().also { dao.save(it) }

        val expected = sample
        val actual = dao.one(siteId = sample.siteId)

        assertEquals(expected, actual)
    }

    @Test
    fun searchLaunchPadTest() = runBlocking {
        val count = 20
        val searchQuery = SearchQuery("Vandenberg")
        val sample = SampleDbData.LaunchPads
            .many()
            .take(20)
            .toList()
            .also { dao.saveAll(it) }

        val expected = sample
            .filter { it.siteNameLong.contains(searchQuery.query) }
            .sortedBy { it.siteNameLong }

        val actual = dao.forQuery(searchQuery.sqlQuery(), limit = count)

        assertEquals(expected, actual)
    }
}