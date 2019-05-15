package com.haroldadmin.moonshot.database.launch

data class LaunchSite(
    val siteId: String,
    val siteName: String,
    val siteNameLong: String
) {
    companion object {
        internal fun getSampleLaunchSite() =
            LaunchSite(
                "ccafs_slc_40",
                "CCAFS SLC 40",
                "Cape Canaveral Air Force Station Space Launch Complex 40"
            )
    }
}