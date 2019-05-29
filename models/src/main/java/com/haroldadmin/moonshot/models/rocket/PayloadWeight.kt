package com.haroldadmin.moonshot.models.rocket

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "payload_weights")
data class PayloadWeight(
    @ColumnInfo(name = "payload_weight_id")
    val payloadWeightId: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "kg")
    val kg: Double,
    @ColumnInfo(name = "lb")
    val lb: Double,
    @ForeignKey(
        entity = Rocket::class,
        parentColumns = ["rocket_id"],
        childColumns = ["rocket_id"],
        onDelete = ForeignKey.CASCADE
    )
    @ColumnInfo(name = "rocket_id")
    val rocketId: String
) {

    @PrimaryKey(autoGenerate = true)
    var id: Int = -1

    companion object {
        fun gameSamplePayloadWeight(rocketId: String): PayloadWeight {
            return PayloadWeight(
                payloadWeightId = "leo",
                name = "Low Earth Orbit",
                kg = 22800.0,
                lb = 52065.0,
                rocketId = rocketId
            )
        }
    }
}