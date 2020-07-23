package com.haroldadmin.moonshot.features.launchPad

import com.haroldadmin.moonshot.models.common.Location

fun Location.getStaticMapUrl(zoom: Int = 13, size: String = "200x200") =
        "https://www.mapquestapi.com/staticmap/v4/getmap?key=${BuildConfig.MapQuestApiKey}" +
                "&size=600,400" +
                "&zoom=$zoom" +
                "&center=$latitude,$longitude" +
                "&pois=1,$latitude,$longitude" +
                "&type=sat"