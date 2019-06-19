package com.haroldadmin.moonshot

import com.haroldadmin.moonshot.base.MoonShotState
import com.haroldadmin.moonshot.core.Consumable

data class ScaffoldingState(
    val toolbarTitle: Consumable<String>
) : MoonShotState