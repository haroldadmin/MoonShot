package com.haroldadmin.services.spacex.v4

import java.net.URL

internal fun Any.getResource(path: String): URL = this::class.java.getResource(path)