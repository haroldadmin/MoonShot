package com.haroldadmin.logger

import io.kotlintest.shouldBe
import io.kotlintest.specs.AnnotationSpec
import java.io.OutputStream
import java.lang.StringBuilder

private class StringOutputStream: OutputStream() {
    private val builder = StringBuilder()

    override fun write(byte: Int) {
        builder.append((byte.toChar()))
    }

    fun reset() = builder.clear()

    override fun toString(): String {
        return builder.toString()
    }
}

internal class LoggerTest: AnnotationSpec() {

    private val outputStream = StringOutputStream()
    private val errorStream = StringOutputStream()

    @Before
    fun setup() {
        Logger.enable()
        Logger.output = outputStream
        Logger.error = errorStream
    }

    @After
    fun cleanup() {
        outputStream.reset()
        errorStream.reset()
    }

    @Test
    fun debugLogTest() {
        logD("foo")
        val output = outputStream.toString()
        val error = errorStream.toString()
        output shouldBe "LoggerTest: foo\n"
        error shouldBe ""
    }

    @Test
    fun errorLogTest() {
        logE("foo")
        val error = errorStream.toString()
        val output = outputStream.toString()
        error shouldBe "LoggerTest: foo\n"
        output shouldBe ""
    }

    @Test
    fun loggingDisabledTest() {
        Logger.disable()
        logD { "foo" }
        logE { "bar" }
        logV { "baz" }

        errorStream.toString() shouldBe ""
        outputStream.toString() shouldBe ""
    }

}