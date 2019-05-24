package com.haroldadmin.moonshot.database.info

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.haroldadmin.moonshot.database.BaseDbTest
import com.haroldadmin.moonshot.models.info.CompanyInfo
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
internal class CompanyDaoTest: BaseDbTest() {

    private val dao by lazy { db.companyInfoDao() }

    @Test
    fun companyInfoWriteTest() = runBlocking {
        val companyInfo = CompanyInfo.getSampleCompanyInfo()
        dao.saveCompanyInfo(companyInfo)

        val savedCompanyInfo = dao.getCompanyInfo()
        assertEquals(companyInfo, savedCompanyInfo)
    }
}