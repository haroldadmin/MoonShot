package com.haroldadmin.moonshot.models

/**
 * Represents a preview of an internet link
 *
 * @param website The name of the website, eg. Twitter, Reddit, YouTube
 * @param url The URL of the website, eg: www.twitter.com,
 * @param title The title fetched from open graph tags
 * @param image The image URL fetched from open graph tags
 * @param description The description fetched from open graph tags
 */
data class LinkPreview(
    val website: String,
    val url: String,
    val title: String?,
    val image: String?,
    val description: String?
)