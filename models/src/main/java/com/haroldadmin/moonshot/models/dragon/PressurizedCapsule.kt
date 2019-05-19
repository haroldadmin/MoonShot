package com.haroldadmin.moonshot.models.dragon

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.haroldadmin.moonshot.models.common.Volume

@Entity(tableName = "pressurized_capsules")
data class PressurizedCapsule(
    @Embedded
    val payloadVol: Volume
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "pressurized_capsule_id")
    var pressurizedCapsuleId: Int? = null
}