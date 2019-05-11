package com.haroldadmin.moonshot.database.core

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "cores")
data class Core(
    @PrimaryKey
    @ColumnInfo(name = "core_serial")
    val serial: String,
    @ColumnInfo(name = "block")
    val block: Int?,
    @ColumnInfo(name = "status")
    val status: String,
    @ColumnInfo(name = "original_launch")
    val originalLaunch: Date?,
    @ColumnInfo(name = "reuse_count")
    val reuseCount: Int,
    @ColumnInfo(name = "rtls_attempts")
    val rtlsAttempts: Int,
    @ColumnInfo(name = "rtls_landings")
    val rtlsLandings: Int,
    @ColumnInfo(name = "asds_attempts")
    val asdsAttempts: Int,
    @ColumnInfo(name = "asds_landings")
    val asdsLandings: Int,
    @ColumnInfo(name = "water_landing")
    val waterLanding: Boolean,
    @ColumnInfo(name = "details")
    val details: String
)
