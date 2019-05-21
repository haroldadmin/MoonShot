package com.haroldadmin.moonshot.models.dragon

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.haroldadmin.moonshot.models.common.Volume

data class Trunk (
    @Embedded
    val trunkVolume: Volume,
    @Embedded
    val cargo: Cargo
)