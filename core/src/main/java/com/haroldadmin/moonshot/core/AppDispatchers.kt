package com.haroldadmin.moonshot.core

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class AppDispatchers(
    val main: CoroutineDispatcher,
    val io: CoroutineDispatcher,
    val cpu: CoroutineDispatcher,
    val unconfined: CoroutineDispatcher
) {
    @Inject
    constructor(): this(
        main = Dispatchers.Main,
        io = Dispatchers.IO,
        cpu = Dispatchers.Default,
        unconfined = Dispatchers.Unconfined
    )
}

class TestDispatchers: AppDispatchers(
    main = Dispatchers.Unconfined,
    io = Dispatchers.Unconfined,
    cpu = Dispatchers.Default,
    unconfined = Dispatchers.Unconfined
)
