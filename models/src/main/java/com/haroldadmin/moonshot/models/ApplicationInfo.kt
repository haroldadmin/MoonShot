package com.haroldadmin.moonshot.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ApplicationInfo(
    @ColumnInfo(name = "last_sync_time")
    val lastSyncTime: Long? = null,
    @PrimaryKey
    val id: String = "moonshot",
    @ColumnInfo(name = "is_first_launch")
    val isFirstLaunch: Boolean = false
)

fun ApplicationInfo?.isFirstLaunch(): Boolean {
    return this == null || this.isFirstLaunch
}