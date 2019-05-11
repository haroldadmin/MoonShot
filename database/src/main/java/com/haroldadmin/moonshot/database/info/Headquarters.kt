package com.haroldadmin.moonshot.database.info

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "headquarters")
data class Headquarters(
    @ColumnInfo(name = "address") val address: String,
    @ColumnInfo(name = "city") val city: String,
    @ColumnInfo(name = "state") val state: String,
    @PrimaryKey(autoGenerate = true) val id: Int = -1
)