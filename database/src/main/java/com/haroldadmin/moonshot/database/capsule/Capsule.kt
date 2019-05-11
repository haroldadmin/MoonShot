package com.haroldadmin.moonshot.database.capsule

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "capsules")
data class Capsule(
    @PrimaryKey
    @ColumnInfo(name = "capsule_serial")
    val serial: String,
    @ColumnInfo(name = "capsule_id")
    val id: String,
    @ColumnInfo(name = "status")
    val status: String,
    @ColumnInfo(name = "original_launch")
    val originalLaunch: Date?,
    @ColumnInfo(name = "landings")
    val landings: Int,
    @ColumnInfo(name = "type")
    val type: String,
    @ColumnInfo(name = "details")
    val details: String?,
    @ColumnInfo(name = "reuse_count")
    val reuseCount: Int
)

