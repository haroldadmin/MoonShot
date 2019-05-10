package com.haroldadmin.spacex_api_wrapper.launches

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Links(
    @field:Json(name = "mission_patch") val missionPatch: String,
    @field:Json(name = "mission_patch_small") val missionPatchSmall: String,
    @field:Json(name = "reddit_campaign") val redditCampaign: String,
    @field:Json(name = "reddit_launch") val redditLaunch: String,
    @field:Json(name = "reddit_recovery") val redditRecovery: String?,
    @field:Json(name = "reddit_media") val redditMedia: String,
    @field:Json(name = "presskit") val pressKit: String,
    @field:Json(name = "article_link") val article: String,
    @field:Json(name = "wikipedia") val wikipedia: String,
    @field:Json(name = "video_link") val video: String,
    @field:Json(name = "youtube_id") val youtubeKey: String,
    @field:Json(name = "flickr_images") val flickrImages: List<String>
)