package com.haroldadmin.moonshot.database.history

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history_links")
data class Links(
    @ColumnInfo(name = "reddit")
    val reddit: String?,
    @ColumnInfo(name = "article")
    val article: String?,
    @ColumnInfo(name = "wikipedia")
    val wikipedia: String?,
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "links_id")
    val linksId: Int = -1
)