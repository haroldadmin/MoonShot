package com.haroldadmin.moonshot.database.launch

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "launch_links")
data class Links(
    @ColumnInfo(name = "mission_patch")
    val missionPatch: String,
    @ColumnInfo(name = "mission_patch_small")
    val missionPatchSmall: String,
    @ColumnInfo(name = "reddit_campaign")
    val redditCampaign: String,
    @ColumnInfo(name = "reddit_launch")
    val redditLaunch: String,
    @ColumnInfo(name = "reddit_recovery")
    val redditRecovery: String?,
    @ColumnInfo(name = "reddit_media")
    val redditMedia: String,
    @ColumnInfo(name = "presskit")
    val presskit: String,
    @ColumnInfo(name = "article_link")
    val article: String,
    @ColumnInfo(name = "wikipedia")
    val wikipedia: String,
    @ColumnInfo(name = "video")
    val video: String,
    @ColumnInfo(name = "youtube_id")
    val youtubeKey: String,
    @ColumnInfo(name = "flickr_images")
    val flickrImages: List<String>
) {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "launch_links_id")
    var id: Int? = null

}