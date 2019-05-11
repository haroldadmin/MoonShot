package com.haroldadmin.spacex_api_wrapper.launches

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Links(
    @Json(name = "mission_patch") val missionPatch: String,
    @Json(name = "mission_patch_small") val missionPatchSmall: String,
    @Json(name = "reddit_campaign") val redditCampaign: String,
    @Json(name = "reddit_launch") val redditLaunch: String,
    @Json(name = "reddit_recovery") val redditRecovery: String?,
    @Json(name = "reddit_media") val redditMedia: String,
    @Json(name = "presskit") val pressKit: String,
    @Json(name = "article_link") val article: String,
    @Json(name = "wikipedia") val wikipedia: String,
    @Json(name = "video_link") val video: String,
    @Json(name = "youtube_id") val youtubeKey: String,
    @Json(name = "flickr_images") val flickrImages: List<String>
)