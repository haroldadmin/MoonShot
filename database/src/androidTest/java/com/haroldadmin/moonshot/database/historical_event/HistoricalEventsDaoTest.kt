package com.haroldadmin.moonshot.database.historical_event

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.haroldadmin.moonshot.database.BaseDbTest
import com.haroldadmin.moonshot.models.history.HistoricalEvent
import com.haroldadmin.moonshot.models.launch.Links
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
internal class HistoricalEventsDaoTest : BaseDbTest() {

    private val historicalEventsDao by lazy { db.historicalEventsDao() }

    @Test
    fun historicalEventsWriteTest() = runBlocking {
        val historicalEvent = HistoricalEvent.getSampleHistoricalEvent()
        val links = Links.getSampleLinks()
        historicalEventsDao.save(historicalEvent)
        val savedHistoricalEvent = historicalEventsDao.getHistoricalEvent(historicalEvent.id)
        val savedLinks = savedHistoricalEvent.links
        assertEquals(historicalEvent, savedHistoricalEvent)
        assertEquals(links, savedLinks)
    }
}