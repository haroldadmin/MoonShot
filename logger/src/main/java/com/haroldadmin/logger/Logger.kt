package com.haroldadmin.logger

import java.io.OutputStream

object Logger {
    internal var output: OutputStream = System.out
    internal var error: OutputStream = System.err

    internal var isEnabled = true

    fun writeTo(out: OutputStream) {
        output = out
    }

    fun enable() {
        isEnabled = true
    }

    fun disable() {
        isEnabled = false
    }
}

fun Any.logD(message: String) {
    if (Logger.isEnabled) {
        Logger.output.write("${this::class.simpleName}: $message\n".toByteArray())
    }
}

fun Any.logE(message: String) {
    if (Logger.isEnabled) {
        Logger.error.write("${this::class.simpleName}: $message\n".toByteArray())
    }
}

fun Any.logV(message: String) {
    if (Logger.isEnabled) {
        Logger.output.write("${this::class.simpleName}: $message\n".toByteArray())
    }
}

fun Any.logD(messageProducer: () -> String) {
    logD(messageProducer())
}

fun Any.logE(messageProducer: () -> String) {
    logE(messageProducer())
}

fun Any.logV(messageProducer: () -> String) {
    logV(messageProducer())
}