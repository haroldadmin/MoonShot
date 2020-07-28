package com.haroldadmin.moonshot.db

import com.haroldadmin.moonshot.db.test.useDatabase
import com.haroldadmin.moonshot.services.spacex.v4.test.APISampleData
import io.kotlintest.matchers.collections.shouldHaveSize
import io.kotlintest.matchers.types.shouldNotBeSameInstanceAs
import io.kotlintest.shouldBe
import io.kotlintest.specs.AnnotationSpec

internal class CapsulesTest: AnnotationSpec() {

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
    @Ignore
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
    @Ignore
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