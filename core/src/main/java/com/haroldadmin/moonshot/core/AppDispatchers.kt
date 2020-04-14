package com.haroldadmin.moonshot.core

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@Suppress("PropertyName")
abstract class AppDispatchers {
    abstract val IO: CoroutineDispatcher
    abstract val Default: CoroutineDispatcher
    abstract val Unconfined: CoroutineDispatcher
    abstract val Main: CoroutineDispatcher
}

data class MoonShotDispatchers(
    override val Default: CoroutineDispatcher,
    override val IO: CoroutineDispatcher,
    override val Unconfined: CoroutineDispatcher,
    override val Main: CoroutineDispatcher
) : AppDispatchers() {

    @Inject
    constructor() : this(
        Default = Dispatchers.Default,
        IO = Dispatchers.IO,
        Unconfined = Dispatchers.Unconfined,
        Main = Dispatchers.Main
    )
}

data class TestDispatchers(
    override val Default: CoroutineDispatcher,
    override val IO: CoroutineDispatcher,
    override val Unconfined: CoroutineDispatcher,
    override val Main: CoroutineDispatcher
) : AppDispatchers() {

    @Inject
    constructor() : this(
        Default = Dispatchers.Unconfined,
        IO = Dispatchers.Unconfined,
        Unconfined = Dispatchers.Unconfined,
        Main = Dispatchers.Unconfined
    )

}

data class ImmediateDispatchers(
    override val Default: CoroutineDispatcher,
    override val IO: CoroutineDispatcher,
    override val Unconfined: CoroutineDispatcher,
    override val Main: CoroutineDispatcher
) : AppDispatchers() {

    @Inject
    constructor() : this(
        Default = Dispatchers.Main.immediate,
        IO = Dispatchers.Main.immediate,
        Unconfined = Dispatchers.Main.immediate,
        Main = Dispatchers.Main.immediate
    )

}