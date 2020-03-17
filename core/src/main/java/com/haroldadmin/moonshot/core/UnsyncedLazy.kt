package com.haroldadmin.moonshot.core

// A simple alias for the Lazy delegate with no thread safety
fun <T> unsyncedLazy(initializer: () -> T) = lazy(LazyThreadSafetyMode.NONE, initializer)