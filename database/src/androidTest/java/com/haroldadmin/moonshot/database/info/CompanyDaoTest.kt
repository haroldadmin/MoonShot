package com.haroldadmin.moonshot.database.info

import com.haroldadmin.moonshot.database.BaseDbTest
import com.haroldadmin.moonshot.models.info.CompanyInfo
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

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