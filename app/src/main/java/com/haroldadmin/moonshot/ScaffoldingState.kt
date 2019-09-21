package com.haroldadmin.moonshot

import android.os.Parcelable
import com.haroldadmin.moonshot.base.MoonShotState
import com.haroldadmin.moonshot.core.Consumable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ScaffoldingState(
    val toolbarTitle: Consumable<String>
) : MoonShotState, Parcelable