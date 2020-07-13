package com.haroldadmin.moonshot.db

import com.haroldadmin.moonshot.services.spacex.v4.test.APISampleData
import io.kotlintest.matchers.types.shouldNotBeSameInstanceAs
import io.kotlintest.shouldBe
import io.kotlintest.specs.AnnotationSpec

internal class DragonsTest: AnnotationSpec() {

    @Test
    fun testDragonMappers() {
        val apiModel = APISampleData.Dragons.samples().first()
        val dbModel = apiModel.toDBModel()

        dbModel.id shouldBe apiModel.id
        dbModel.name shouldBe apiModel.name
        dbModel.type shouldBe apiModel.type
        dbModel.active shouldBe apiModel.active
        dbModel.crewCapacity shouldBe apiModel.crewCapacity
        dbModel.sidewallAngleDeg shouldBe apiModel.sidewallAngleDegrees
        dbModel.orbitDurationYears shouldBe apiModel.orbitDurationYears
        dbModel.dryMassKg shouldBe apiModel.dryMassKg
        dbModel.dryMassLb shouldBe apiModel.dryMassLb
        dbModel.firstFlight shouldBe apiModel.firstFlight
        dbModel.heatShield_material shouldBe apiModel.heatShield?.material
        dbModel.heatShield_sizeMetres shouldBe apiModel.heatShield?.sizeMetre
        dbModel.heatShield_tempDegrees shouldBe apiModel.heatShield?.tempDegree
        dbModel.heatShield_devPartner shouldBe apiModel.heatShield?.devPartner
        dbModel.launchPayloadMass_kg shouldBe apiModel.launchPayloadMass?.kg
        dbModel.launchPayloadMass_lb shouldBe apiModel.launchPayloadMass?.lb
        dbModel.launchPayloadVol_cubicMetres shouldBe apiModel.launchPayloadVolume?.cubicMetres
        dbModel.launchPayloadVol_cubicFeet shouldBe apiModel.launchPayloadVolume?.cubicFeet
        dbModel.returnPayloadMass_kg shouldBe apiModel.returnPayloadMass?.kg
        dbModel.returnPayloadMass_lb shouldBe apiModel.returnPayloadMass?.lb
        dbModel.returnPayloadVol_cubicMetres shouldBe apiModel.returnPayloadVolume?.cubicMetres
        dbModel.returnPayloadVol_cubicFeet shouldBe apiModel.returnPayloadVolume?.cubicFeet
        dbModel.pressurizedCapsule_payloadVolume_cubicMetres shouldBe apiModel.pressurizedCapsule?.payloadVolume?.cubicMetres
        dbModel.pressurizedCapsule_payloadVolume_cubicFeet shouldBe apiModel.pressurizedCapsule?.payloadVolume?.cubicFeet
        dbModel.trunk_trunkVolume_cubicMetres shouldBe apiModel.trunk?.trunkVolume?.cubicMetres
        dbModel.trunk_trunkVolume_cubicFeet shouldBe apiModel.trunk?.trunkVolume?.cubicFeet
        dbModel.trunk_cargo_solarArray shouldBe apiModel.trunk?.cargo?.solarArray
        dbModel.trunk_cargo_unpressurizedCargo shouldBe apiModel.trunk?.cargo?.unpressurizedCargo
        dbModel.heightWithTrunk_metres shouldBe apiModel.heightWithTrunk?.metres
        dbModel.heightWithTrunk_feet shouldBe apiModel.heightWithTrunk?.feet
        dbModel.diameter_metres shouldBe apiModel.diameter?.metres
        dbModel.diameter_feet shouldBe apiModel.diameter?.feet
        dbModel.flickrImages shouldBe apiModel.flickrImages
        dbModel.flickrImages shouldNotBeSameInstanceAs apiModel.flickrImages
        dbModel.wikipedia shouldBe apiModel.wikipedia
        dbModel.description shouldBe apiModel.description
    }

    @Test
    fun testThrusterMapper() {
        val dragon = APISampleData.Dragons.samples().first()
        val apiModel = dragon.thrusters.first()
        val dbModel = apiModel.toDBModel(dragon.id)

        dbModel.type shouldBe apiModel.type
        dbModel.amount shouldBe apiModel.amount
        dbModel.pods shouldBe apiModel.pods
        dbModel.fuelOne shouldBe apiModel.fuelOne
        dbModel.fuelTwo shouldBe apiModel.fuelTwo
        dbModel.isp shouldBe apiModel.isp
        dbModel.thrust_kN shouldBe apiModel.thrust?.kN
        dbModel.thrust_lbf shouldBe apiModel.thrust?.lbf
        dbModel.dragonID shouldBe dragon.id
    }

}