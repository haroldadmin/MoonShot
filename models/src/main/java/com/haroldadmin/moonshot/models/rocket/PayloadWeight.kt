package com.haroldadmin.moonshot.models.rocket

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "payload_weights")
data class PayloadWeight (
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
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
)