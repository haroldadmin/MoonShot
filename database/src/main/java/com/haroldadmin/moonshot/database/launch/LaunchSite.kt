package com.haroldadmin.moonshot.database.launch

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "launch_sites")
data class LaunchSite(
    @PrimaryKey
    @ColumnInfo(name = "site_id") val siteId: String,
    @ColumnInfo(name = "site_name") val siteName: String,
    @ColumnInfo(name = "site_name_long") val siteNameLong: String
)