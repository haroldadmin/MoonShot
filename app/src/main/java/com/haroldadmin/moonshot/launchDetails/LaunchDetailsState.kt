package com.haroldadmin.moonshot.launchDetails

import android.os.Parcelable
import com.haroldadmin.moonshot.base.MoonShotState
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.models.launch.Launch
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LaunchDetailsArgs(
    val flightNumber: Int
): Parcelable

data class LaunchDetailsState(
    val flightNumber: Int,
    val launch: Resource<Launch> = Resource.Uninitialized
): MoonShotState {
    constructor(args: LaunchDetailsArgs): this(args.flightNumber)
}