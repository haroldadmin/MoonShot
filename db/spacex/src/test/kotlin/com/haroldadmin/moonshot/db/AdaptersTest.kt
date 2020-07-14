package com.haroldadmin.moonshot.db

import io.kotlintest.matchers.collections.shouldHaveSize
import io.kotlintest.shouldBe
import io.kotlintest.specs.AnnotationSpec
import java.time.LocalDate
import java.time.Month
import java.time.ZonedDateTime

internal class AdaptersTest : AnnotationSpec() {
    @Test
    fun `launchID adapter encoding test`() {
        val adapter = ListToStringAdapter()
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
        val adapter = ListToStringAdapter()
        val launchIDs = listOf<String>()
        val encodedValue = adapter.encode(launchIDs)

        encodedValue shouldBe ""
    }

    @Test
    fun `launchID adapter decoding test`() {
        val adapter = ListToStringAdapter()
        val ids = "5eb87cdeffd86e000604b330,5eb87cdeffd86e000604b330,5eb87cdeffd86e000604b330"
        val launchIDs = adapter.decode(ids)

        launchIDs shouldHaveSize 3
        launchIDs.forEach { it shouldBe "5eb87cdeffd86e000604b330" }
    }

    @Test
    fun `launchID adapter decoding test for empty string`() {
        val adapter = ListToStringAdapter()
        val ids = ""
        val encodedValue = adapter.decode(ids)

        encodedValue shouldBe listOf()
    }

    @Test
    fun `local date adapter encoding test`() {
        val adapter = LocalDateAdapter()
        val expectedDate = LocalDate.parse("2010-12-08")
        val encodedValue = adapter.encode(expectedDate)

        encodedValue shouldBe expectedDate.toString()
    }

    @Test
    fun `local date adapter decoding test`() {
        val adapter = LocalDateAdapter()
        val expectedDate = "2010-12-08"
        val decodedValue = adapter.decode(expectedDate)

        decodedValue shouldBe LocalDate.parse(expectedDate)
    }

    @Test
    fun `should decode empty string as 1970-01-01`() {
        val adapter = LocalDateAdapter()
        val date = adapter.decode("")
        date.year shouldBe 1970
        date.month shouldBe Month.JANUARY
        date.dayOfMonth shouldBe 1
    }

    @Test
    fun `zoned date time adapter encoding test`() {
        val adapter = ZonedDateTimeAdapter()
        val isoVal = "2020-03-06T23:50:31-05:00"

        val zonedDateTime = ZonedDateTime.parse(isoVal)
        val encodedValue = adapter.encode(zonedDateTime)

        encodedValue shouldBe isoVal
    }

    @Test
    fun `zoned date time adapter decoding test`() {
        val adapter = ZonedDateTimeAdapter()
        val isoVal = "2020-03-06T23:50:31-05:00"
        val zonedDateTime = ZonedDateTime.parse(isoVal)

        zonedDateTime.toString() shouldBe isoVal
    }
}