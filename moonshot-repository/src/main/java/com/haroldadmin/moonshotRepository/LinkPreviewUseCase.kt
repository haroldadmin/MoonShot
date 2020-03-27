package com.haroldadmin.moonshotRepository

import android.os.Build
import android.util.Log
import com.haroldadmin.moonshot.core.AppDispatchers
import com.haroldadmin.moonshot.models.LinkPreview
import com.haroldadmin.opengraphKt.Tags
import com.haroldadmin.opengraphKt.getOpenGraphTags
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL
import javax.inject.Inject

class LinkPreviewUseCase @Inject constructor(
    private val appDispatchers: AppDispatchers
) {

    private val TAG = "LinkPreviewUseCase"

    suspend fun getPreview(website: String, link: String): LinkPreview = withContext(appDispatchers.IO) {
        try {
            val url = URL(link)
            val tags = url.getOpenGraphTags()
            tags.toLinkPreview(website, link)
        } catch (ex: MalformedURLException) {
            LinkPreview(website, link, null, null, null)
        } catch (ex: IOException) {
            if (isClearTextCommunicationDenied(link)) {
                Log.e(TAG, "Cleartext communication to $link might have been denied")
            } else {
                ex.printStackTrace()
            }
            LinkPreview(website, link, null, null, null)
        }
    }

    suspend fun getPreviews(
        websiteNamesAndLinks: Map<String, String>
    ): List<LinkPreview> = withContext(appDispatchers.IO) {
        websiteNamesAndLinks
            .map { (websiteName, link) -> async { getPreview(websiteName, link) } }
            .awaitAll()
    }

    private fun isClearTextCommunicationDenied(link: String): Boolean {
        return URL(link).protocol == "http" && Build.VERSION.SDK_INT >= Build.VERSION_CODES.P
    }
}

private fun Tags.toLinkPreview(website: String, url: String): LinkPreview {
    return LinkPreview(website, url, this.title, this.image, this.description)
}