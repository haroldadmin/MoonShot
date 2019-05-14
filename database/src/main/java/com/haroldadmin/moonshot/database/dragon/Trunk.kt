package com.haroldadmin.moonshot.database.dragon

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.haroldadmin.moonshot.database.common.Volume

@Entity(tableName = "trunks")
data class Trunk (
    @Embedded
    val trunkVolume: Volume,
    @Embedded
    val cargo: Cargo
) {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "trunk_id")
    var trunkId: Int? = null

}