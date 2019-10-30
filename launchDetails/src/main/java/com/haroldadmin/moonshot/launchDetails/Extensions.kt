package com.haroldadmin.moonshot.launchDetails

internal fun String.youtubeThumbnail(): String {
    return "https://img.youtube.com/vi/$this/0.jpg"
}

internal fun String.youtubeVideo(): String {
    return "https://www.youtube.com/watch?v=$this"
}