package com.haroldadmin.moonshot.database

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.haroldadmin.moonshot.models.ApplicationInfo
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Date

@RunWith(AndroidJUnit4::class)
internal class ApplicationInfoDaoTest : DaoTest() {

    private val dao by dbRule.dao<ApplicationInfoDao>()

    @Test
    fun readWriteTest() = runBlocking {
        val lastSyncTime = Date().time
        val applicationInfo = ApplicationInfo(lastSyncTime = lastSyncTime).also {
            dao.save(it)
        }

        val expected = applicationInfo
        val actual = dao.applicationInfo()

        assertEquals(expected, actual)
    }

    @Test
    fun updateTest() = runBlocking {
        var lastSyncTime = Date().time
        val applicationInfo = ApplicationInfo(lastSyncTime = lastSyncTime).also {
            dao.save(it)
        }

        lastSyncTime++
        val updatedApplicationInfo = ApplicationInfo(lastSyncTime = lastSyncTime).also {
            dao.update(it)
        }

        assertNotEquals(applicationInfo, updatedApplicationInfo)
    }
}