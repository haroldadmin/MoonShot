package com.haroldadmin.moonshot.models.info

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "companyInfo")
data class CompanyInfo(
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
) {

    companion object {
        fun getSampleCompanyInfo(): CompanyInfo {
            return CompanyInfo(
                name = "SpaceX",
                founder = "ElonMusk",
                foundedYear = 2002,
                employees = 7000,
                vehicles = 3,
                launchSites = 3,
                testSites = 3,
                ceo = "Elon Musk",
                cto = "Elon Musk",
                coo = "Gwynne Shotwell",
                ctoPropulsion = "Tom Mueller",
                valuation = 15000000000,
                headquarters = Headquarters.getSampleHeadquarters(),
                summary = "SpaceX designs, manufactures and launches advanced rockets and spacecraft. The company was founded in 2002 to revolutionize space technology, with the ultimate goal of enabling people to live on other planets."
            )
        }
    }

}
