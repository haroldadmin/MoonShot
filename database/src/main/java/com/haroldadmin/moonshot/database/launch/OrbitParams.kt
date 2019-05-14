package com.haroldadmin.moonshot.database.launch

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "orbit_params")
data class OrbitParams(
    @ColumnInfo(name = "reference_system")
    val referenceSystem: String,
    @ColumnInfo(name = "regime")
    val regime: String,
    @ColumnInfo(name = "longitude")
    val longitude: Double?,
    @ColumnInfo(name = "semi_major_axis_km")
    val semiMajorAxisKm: Double?,
    @ColumnInfo(name = "eccentricity")
    val eccentricity: Double?,
    @ColumnInfo(name = "periapsis_km")
    val periapsisKm: Double,
    @ColumnInfo(name = "apoapsis_km")
    val apoapsis: Double,
    @ColumnInfo(name = "inclination_deg")
    val inclinationDeg: Double,
    @ColumnInfo(name = "period_min")
    val periodMin: Double?,
    @ColumnInfo(name = "lifespan_years")
    val lifespanYears: Int?,
    @ColumnInfo(name = "epoch")
    val epoch: Date?,
    @ColumnInfo(name = "mean_motion")
    val meanMotion: Double?,
    @ColumnInfo(name = "raan")
    val raan: Double?,
    @ColumnInfo(name = "arg_of_pericenter")
    val argOfPericenter: Double?,
    @ColumnInfo(name = "mean_anomaly")
    val meanAnomaly: Double?
) {

    @ColumnInfo(name = "orbit_params_id")
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null

}