package com.haroldadmin.moonshot.database.info

import androidx.room.Dao
import androidx.room.Query
import com.haroldadmin.moonshot.database.BaseDao
import com.haroldadmin.moonshot.models.info.CompanyInfo

@Dao
abstract class CompanyInfoDao : BaseDao<CompanyInfo> {

    @Query("SELECT * FROM companyInfo LIMIT 1")
    abstract suspend fun getCompanyInfo(): CompanyInfo

}