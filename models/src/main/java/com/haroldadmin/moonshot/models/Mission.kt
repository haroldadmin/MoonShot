package com.haroldadmin.moonshot.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.haroldadmin.moonshot.models.launch.buildMap
import com.haroldadmin.moonshot.models.launch.putIfNotNull

@Entity
data class Mission(
    @ColumnInfo(name = "mission_name")
    val name: String,
    @PrimaryKey
    @ColumnInfo(name = "mission_id")
    val id: String,
    @ColumnInfo(name = "manufacturers")
    val manufacturers: List<String>,
    @ColumnInfo(name = "payload_ids")
    val payloadIds: List<String>,
    @ColumnInfo(name = "wikipedia")
    val wikipedia: String?,
    @ColumnInfo(name = "website")
    val website: String?,
    @ColumnInfo(name = "twitter")
    val twitter: String?,
    @ColumnInfo(name = "description")
    val description: String?
) {
    @Ignore
    val linksToPreview: Map<String, String> = buildMap {
        putIfNotNull("Website", website)
        putIfNotNull("Wikipedia", wikipedia)
        putIfNotNull("Twitter", twitter)
    }
}
