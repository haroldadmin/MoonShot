package com.haroldadmin.moonshot.models.dragon

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.haroldadmin.moonshot.models.common.Thrust

@Entity(
    tableName = "thrusters",
    foreignKeys = [
        ForeignKey(
            entity = Dragon::class,
            parentColumns = ["dragon_id"],
            childColumns = ["dragon_id"]
        )
    ],
    indices = [Index("dragon_id")]
)
data class Thruster(
    @ColumnInfo(name = "type")
    val type: String,
    @ColumnInfo(name = "amount")
    val amount: Int,
    @ColumnInfo(name = "pods")
    val pods: Int,
    @ColumnInfo(name = "fuel_1")
    val fuel1: String,
    @ColumnInfo(name = "fuel_2")
    val fuel2: String,
    @Embedded
    val thrust: Thrust,
    @ColumnInfo(name = "dragon_id")
    val dragonId: String
) {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "thruster_id")
    var thrusterId: Int? = null

    companion object {
        fun getSampleThruster(dragonId: String): Thruster {
            return Thruster(
                type = "Draco",
                amount = 18,
                pods = 4,
                fuel1 = "nitrogen tetroxide",
                fuel2 = "monomethylhydrazine",
                thrust = Thrust(kN = 0.4, lbf = 90.0),
                dragonId = dragonId
            )
        }
    }

}