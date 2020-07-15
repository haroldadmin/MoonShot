package com.haroldadmin.moonshot.db

import com.haroldadmin.moonshot.services.spacex.v4.test.APISampleData
import io.kotlintest.matchers.types.shouldNotBeSameInstanceAs
import io.kotlintest.shouldBe
import io.kotlintest.specs.AnnotationSpec

internal class RocketsTest: AnnotationSpec() {

    @Test
    fun testMapper() {
        val apiModel = APISampleData.Rockets.samples().first()
        val dbModel = apiModel.toDBModel()

        dbModel.id shouldBe apiModel.id
        dbModel.name shouldBe apiModel.name
        dbModel.type shouldBe apiModel.type
        dbModel.active shouldBe apiModel.active
        dbModel.stages shouldBe apiModel.stages
        dbModel.boosters shouldBe apiModel.boosters
        dbModel.costPerLaunch shouldBe apiModel.costPerLaunch
        dbModel.successRatePercentage shouldBe apiModel.successRatePercentage
        dbModel.firstFlight shouldBe apiModel.firstFlight
        dbModel.country shouldBe  apiModel.country
        dbModel.company shouldBe apiModel.company
        dbModel.height_metres shouldBe apiModel.height?.metres
        dbModel.height_feet shouldBe apiModel.height?.feet
        dbModel.diameter_metres shouldBe apiModel.diameter?.metres
        dbModel.diameter_feet shouldBe apiModel.diameter?.feet
        dbModel.mass_kg shouldBe apiModel.mass?.kg
        dbModel.mass_lb shouldBe apiModel.mass?.lb
        dbModel.firstStage_reusable shouldBe  apiModel.firstStage?.reusable
        dbModel.firstStage_engines shouldBe apiModel.firstStage?.engines
        dbModel.firstStage_fuelAmountTons shouldBe apiModel.firstStage?.fuelAmountTons
        dbModel.firstStage_burnTimeSec shouldBe apiModel.firstStage?.burnTimeSec
        dbModel.firstStage_thrustSeaLevel_kN shouldBe apiModel.firstStage?.thrustSeaLevel?.kN
        dbModel.firstStage_thrustSeaLevel_lbf shouldBe apiModel.firstStage?.thrustSeaLevel?.lbf
        dbModel.firstStage_thrustVacuum_kN shouldBe apiModel.firstStage?.thrustVacuum?.kN
        dbModel.firstStage_thrustVacuum_lbf shouldBe apiModel.firstStage?.thrustVacuum?.lbf
        dbModel.secondStage_reusable shouldBe apiModel.secondStage?.reusable
        dbModel.secondStage_fuelAmountTons shouldBe apiModel.secondStage?.fuelAmountTons
        dbModel.secondStage_thrust_kN shouldBe apiModel.secondStage?.thrust?.kN
        dbModel.secondStage_thrust_lbf shouldBe apiModel.secondStage?.thrust?.lbf
        dbModel.secondStage_burnTimeSec shouldBe apiModel.secondStage?.burnTimeSec
        dbModel.secondStage_payloads_optionOne shouldBe apiModel.secondStage?.payloads?.optionOne
        dbModel.secondStage_payloads_compositeFairing_height_metres shouldBe apiModel.secondStage?.payloads?.compositeFairing?.height?.metres
        dbModel.secondStage_payloads_compositeFairing_height_feet shouldBe apiModel.secondStage?.payloads?.compositeFairing?.height?.feet
        dbModel.engines_number shouldBe apiModel.engines?.number
        dbModel.engines_type shouldBe apiModel.engines?.type
        dbModel.engines_version shouldBe apiModel.engines?.version
        dbModel.engines_layout shouldBe apiModel.engines?.layout
        dbModel.engines_isp_seaLevel shouldBe apiModel.engines?.isp?.seaLevel
        dbModel.engines_isp_vacuum shouldBe apiModel.engines?.isp?.vacuum
        dbModel.engines_engineLossMax shouldBe apiModel.engines?.engineLossMax
        dbModel.engines_propellantOne shouldBe apiModel.engines?.propellantOne
        dbModel.engines_propellantTwo shouldBe apiModel.engines?.propellantTwo
        dbModel.engines_thrustSeaLevel_kN shouldBe apiModel.engines?.thrustSeaLevel?.kN
        dbModel.engines_thrustSeaLevel_lbf shouldBe apiModel.engines?.thrustSeaLevel?.lbf
        dbModel.engines_thrustVacuum_kN shouldBe apiModel.engines?.thrustVacuum?.kN
        dbModel.engines_thrustVacuum_lbf shouldBe apiModel.engines?.thrustVacuum?.lbf
        dbModel.engines_thrustToWeight shouldBe apiModel.engines?.thrustToWeight
        dbModel.landingLegs_material shouldBe apiModel.landingLegs?.material
        dbModel.landingLegs_number shouldBe apiModel.landingLegs?.number
        dbModel.flickrImages shouldBe apiModel.flickrImages
        dbModel.flickrImages shouldNotBeSameInstanceAs apiModel.flickrImages
        dbModel.wikipedia shouldBe apiModel.wikipedia
        dbModel.description shouldBe apiModel.description
    }

    @Test
    fun testRocketPayloadMapper() {
        val rocket = APISampleData.Rockets.samples().first()
        val apiModel = rocket.payloadWeights.first()
        val dbModel = apiModel.toDBModel(rocket.id)

        dbModel.id shouldBe apiModel.id
        dbModel.name shouldBe apiModel.name
        dbModel.kg shouldBe apiModel.kg
        dbModel.lb shouldBe apiModel.lb
        dbModel.rocketID shouldBe rocket.id
    }

}