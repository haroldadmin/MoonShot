package com.haroldadmin.spacex_api_wrapper.dragon

import com.haroldadmin.spacex_api_wrapper.common.Volume
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PressurizedCapsule(
    @Json(name = "payload_volume") val payloadVol: Volume
)