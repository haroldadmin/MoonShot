package com.haroldadmin.moonshot.database.dragon

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cargos")
data class Cargo(
    @ColumnInfo(name = "solar_array")
    val solarArray: Int,
    @ColumnInfo(name = "unpressurized_cargo")
    val unpressurizedCargo: Boolean,
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "cargo_id")
    val cargoId: Int = -1
)