package com.haroldadmin.moonshot.models.launch

data class Links(
    val missionPatch: String?,
    val missionPatchSmall: String?,
    val redditCampaign: String?,
    val redditLaunch: String?,
    val redditRecovery: String?,
    val redditMedia: String?,
    val presskit: String?,
    val article: String?,
    val wikipedia: String?,
    val video: String?,
    val youtubeKey: String?,
    val flickrImages: List<String>?
) {

    companion object {
        fun getSampleLinks() = Links(
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            listOf()
        )
    }
}