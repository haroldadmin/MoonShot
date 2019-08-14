package com.haroldadmin.moonshot.search

import org.koin.core.context.loadKoinModules

object Search {
    private val loadModules: Unit by lazy {
        loadKoinModules(searchModule)
    }

    fun init() = loadModules
}