package com.haroldadmin.moonshot.models.info

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "companyInfo")
data class CompanyInfo (
    @PrimaryKey
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "founder") val founder: String,
    @ColumnInfo(name = "founded") val foundedYear: Int,
    @ColumnInfo(name = "employees") val employees: Int,
    @ColumnInfo(name = "vehicles") val vehicles: Int,
    @ColumnInfo(name = "launch_sites") val launchSites: Int,
    @ColumnInfo(name = "test_sites") val testSites: Int,
    @ColumnInfo(name = "ceo") val ceo: String,
    @ColumnInfo(name = "cto") val cto: String,
    @ColumnInfo(name = "coo") val coo: String,
    @ColumnInfo(name = "cto_propulsion") val ctoPropulsion: String,
    @ColumnInfo(name = "valuation") val valuation: Long,
    @Embedded val headquarters: Headquarters,
    @ColumnInfo(name = "summary") val summary: String
)
