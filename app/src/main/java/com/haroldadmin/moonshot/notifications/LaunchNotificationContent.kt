package com.haroldadmin.moonshot.notifications

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LaunchNotificationContent(
    val name: String,
    val site: String,
    val date: String,
    val time: Long, // Millis since epoch in UTC
    val missionPatch: String?,
    val flightNumber: Int
) : Parcelable
