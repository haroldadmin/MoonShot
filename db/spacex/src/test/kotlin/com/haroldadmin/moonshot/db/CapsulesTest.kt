package com.haroldadmin.moonshot.db

import com.haroldadmin.moonshot.services.spacex.v4.test.APISampleData
import io.kotlintest.matchers.collections.shouldHaveSize
import io.kotlintest.matchers.types.shouldNotBeSameInstanceAs
import io.kotlintest.shouldBe
import io.kotlintest.specs.AnnotationSpec

internal class CapsulesTest: AnnotationSpec() {

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

    @Test
    fun testCapsuleMapper() {
        val apiCapsule = APISampleData.Capsules.samples().first()
        val dbCapsule = apiCapsule.toDBModel()

        dbCapsule.id shouldBe apiCapsule.id
        dbCapsule.serial shouldBe apiCapsule.serial
        dbCapsule.status shouldBe apiCapsule.status
        dbCapsule.dragon shouldBe apiCapsule.dragon
        dbCapsule.reuseCount shouldBe apiCapsule.reuseCount
        dbCapsule.waterLandings shouldBe apiCapsule.waterLandings
        dbCapsule.landLandings shouldBe apiCapsule.landLandings
        dbCapsule.lastUpdate shouldBe apiCapsule.lastUpdate
        dbCapsule.launchIDs shouldBe apiCapsule.launchIDs
        dbCapsule.launchIDs shouldNotBeSameInstanceAs apiCapsule.launchIDs
    }

    @Test
    fun `insert Capsule test`() {
        val (db, cleanup) = useDatabase()

        db.capsuleQueries.all().executeAsList() shouldHaveSize 0

        val capsule = APISampleData.Capsules.samples().first()
//        db.capsuleQueries.save(capsule)

        db.capsuleQueries.all().executeAsList() shouldHaveSize 1
        db.capsuleQueries.one(capsule.id).executeAsOne() shouldBe capsule

        cleanup()
    }

    @Test
    fun `delete Capsule test`() {
        val (db, cleanup) = useDatabase()

        val capsule = APISampleData.Capsules.samples().first()
//        db.capsuleQueries.save(capsule)
        db.capsuleQueries.all().executeAsList() shouldHaveSize 1

        db.capsuleQueries.delete(capsule.id)
        db.capsuleQueries.all().executeAsList() shouldHaveSize 0

        cleanup()
    }
}