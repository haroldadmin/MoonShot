package com.haroldadmin.moonshot.models.history

data class Links(
    val reddit: String?,
    val article: String?,
    val wikipedia: String?
) {

    companion object {
        fun getSampleLinks(): Links {
            return Links(
                reddit = null,
                article = "http://www.spacex.com/news/2013/02/11/flight-4-launch-update-0",
                wikipedia = "https://en.wikipedia.org/wiki/Falcon_1"
            )
        }
    }
}