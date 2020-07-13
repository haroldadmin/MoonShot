package com.haroldadmin.moonshot.db

import io.kotlintest.matchers.collections.shouldHaveSize
import io.kotlintest.shouldBe
import io.kotlintest.specs.AnnotationSpec

internal class AdaptersTest : AnnotationSpec() {
    @Test
    fun `launchID adapter encoding test`() {
        val adapter = LaunchIDAdapter()
        val launchIDs = listOf(
            "5eb87cdeffd86e000604b330",
            "5eb87cdeffd86e000604b330",
            "5eb87cdeffd86e000604b330"
        )
        val encodedValue = adapter.encode(launchIDs)

        encodedValue shouldBe "5eb87cdeffd86e000604b330,5eb87cdeffd86e000604b330,5eb87cdeffd86e000604b330"
    }

    @Test
    fun `launchID adapter encoding test for empty lists`() {
        val adapter = LaunchIDAdapter()
        val launchIDs = listOf<String>()
        val encodedValue = adapter.encode(launchIDs)

        encodedValue shouldBe ""
    }

    @Test
    fun `launchID adapter decoding test`() {
        val adapter = LaunchIDAdapter()
        val ids = "5eb87cdeffd86e000604b330,5eb87cdeffd86e000604b330,5eb87cdeffd86e000604b330"
        val launchIDs = adapter.decode(ids)

        launchIDs shouldHaveSize 3
        launchIDs.forEach { it shouldBe "5eb87cdeffd86e000604b330" }
    }

    @Test
    fun `launchID adapter decoding test for empty string`() {
        val adapter = LaunchIDAdapter()
        val ids = ""
        val encodedValue = adapter.decode(ids)

        encodedValue shouldBe listOf()
    }
}