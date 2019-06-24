package com.haroldadmin.moonshot.launchDetails

fun String.youtubeThumbnail(): String {
    return "https://img.youtube.com/vi/$this/0.jpg"
}

fun String.youtubeVideo(): String {
    return "https://www.youtube.com/watch?v=$this"
}