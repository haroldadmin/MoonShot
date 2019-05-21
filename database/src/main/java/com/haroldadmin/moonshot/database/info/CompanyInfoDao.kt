package com.haroldadmin.moonshot.database.info

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.haroldadmin.moonshot.models.info.CompanyInfo

@Dao
interface CompanyInfoDao {

    @Query("SELECT * FROM companyInfo LIMIT 1")
    suspend fun getCompanyInfo(): CompanyInfo

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCompanyInfo(companyInfo: CompanyInfo)

    @Delete
    suspend fun deleteCompanyInfo(companyInfo: CompanyInfo)
}