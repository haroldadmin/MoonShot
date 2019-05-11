package com.haroldadmin.spacex_api_wrapper

import java.net.URL

fun Any.getResource(path: String): URL = this::class.java.getResource(path)