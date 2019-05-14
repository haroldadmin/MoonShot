package com.haroldadmin.moonshot.database.common

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.haroldadmin.moonshot.database.capsule.Capsule
import com.haroldadmin.moonshot.database.core.Core

@Entity(
    tableName = "mission_summaries",
    foreignKeys = [
        ForeignKey(
            entity = Capsule::class,
            parentColumns = ["capsule_serial"],
            childColumns = ["capsule_serial"]
        ),
        ForeignKey(
            entity = Core::class,
            parentColumns = ["core_serial"],
            childColumns = ["core_serial"]
        )
    ],
    indices = [
        Index("core_serial"),
        Index("capsule_serial")
    ]
)
data class MissionSummary(
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "flight")
    val flight: Int,
    @ColumnInfo(name = "capsule_serial")
    val capsuleSerial: String?,
    @ColumnInfo(name = "core_serial")
    val coreSerial: String?
) {

    @PrimaryKey(autoGenerate = true)
    var id: Int? = null

    companion object {
        fun getSampleMissionSummary(capsuleSerial: String = "", coreSerial: String = "") = MissionSummary("", 0, capsuleSerial, coreSerial)
    }

}