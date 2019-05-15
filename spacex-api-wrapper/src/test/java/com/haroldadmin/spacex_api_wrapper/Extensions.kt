package com.haroldadmin.spacex_api_wrapper

import okhttp3.mockwebserver.MockResponse
import java.net.URL

fun Any.getResource(path: String): URL = this::class.java.getResource(path)

fun MockResponse.fromFile(path: String, responseCode: Int = 200) = this.apply {
    setBody(getResource(path).readText())
    setResponseCode(responseCode)
}