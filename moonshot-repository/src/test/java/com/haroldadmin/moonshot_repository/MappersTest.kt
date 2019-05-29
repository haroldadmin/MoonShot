package com.haroldadmin.moonshot_repository

import com.haroldadmin.moonshot_repository.mappers.*
import com.haroldadmin.spacex_api_wrapper.common.*
import com.haroldadmin.spacex_api_wrapper.rocket.*
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec

internal class MappersTest : DescribeSpec({
    describe("Common Mappers") {

        context("Mass") {
            val apiMass = Mass(kg = 0.0, lb = 0.0)
            val dbMass = apiMass.toDbMass()

            it("Should map values correctly") {
                dbMass.kg shouldBe apiMass.kg
                dbMass.lb shouldBe apiMass.lb
            }
        }

        context("Volume") {
            val apiVol = Volume(0.0, 0.0)
            val dbVol = apiVol.toDbVolume()

            it("Should map values correctly") {
                dbVol.cubicMeters shouldBe apiVol.cubicMeters
                dbVol.cubicFeet shouldBe apiVol.cubicFeet
            }
        }

        context("Length") {
            val apiLength = Length(0.0, 0.0)
            val dbLength = apiLength.toDbLength()

            it("Should map values correctly") {
                dbLength.meters shouldBe apiLength.meters
                dbLength.feet shouldBe apiLength.feet
            }
        }

        context("Thrust") {
            val apiThrust = Thrust(0.0, 0.0)
            val dbThrust = apiThrust.toDbThrust()

            it("Should map values correctly") {
                dbThrust.kN shouldBe apiThrust.kN
                dbThrust.lbf shouldBe apiThrust.lbf
            }
        }

        context("Location") {
            val apiLoc = Location(
                name = "Test Location",
                region = "Test Region",
                latitude = 0.0,
                longitude = 0.0
            )
            val dbLoc = apiLoc.toDbLocation()

            it("Should map values correctly") {
                dbLoc.latitude shouldBe apiLoc.latitude
                dbLoc.longitude shouldBe apiLoc.longitude
                dbLoc.name shouldBe apiLoc.name
                dbLoc.region shouldBe apiLoc.region
            }
        }

        context("Mission summary") {
            val apiMs = MissionSummary(
                name = "Test mission",
                flight = 0
            )

            val capSrl = "Serial 1"
            val coreSrl = "Serial 2"

            val dbMs = apiMs.toDbMissionSummary(capSrl, coreSrl)

            it("Should map values correctly") {
                with(dbMs) {
                    capsuleSerial shouldBe capSrl
                    coreSerial shouldBe coreSrl
                    name shouldBe apiMs.name
                    flight shouldBe apiMs.flight
                }
            }
        }
    }

    describe("Rocket Mappers") {

        context("Landing Legs") {
            val apiLL = LandingLegs(0, "Test Material")
            val dbLL = apiLL.toDbLandingLegs()

            it("Should map values correctly") {
                dbLL.number shouldBe apiLL.number
                dbLL.material shouldBe apiLL.material
            }
        }

        context("Engines") {
            val apiEngine = Engines(
                number = 0,
                type = "Test Type",
                version = "Test Version",
                layout = "Test Layout",
                engineLossMax = 0,
                propellant2 = "Test Propellant",
                propellant1 = "Test Propellant",
                thrustSeaLevel = Thrust(0.0, 0.0),
                thrustVacuum = Thrust(0.0, 0.0),
                thrustToWeightRatio = 0.0
            )

            val dbEngine = apiEngine.toDbEngine()

            it("Should map values correctly") {
                with(dbEngine) {
                    number shouldBe apiEngine.number
                    type shouldBe apiEngine.type
                    version shouldBe apiEngine.version
                    layout shouldBe apiEngine.layout
                    engineLossMax shouldBe apiEngine.engineLossMax
                    propellant1 shouldBe apiEngine.propellant1
                    propellant2 shouldBe apiEngine.propellant2
                    thrustSeaLevel shouldBe apiEngine.thrustSeaLevel.toDbThrust()
                    thrustVacuum shouldBe apiEngine.thrustVacuum.toDbThrust()
                    thrustToWeight shouldBe apiEngine.thrustToWeightRatio
                }
            }
        }

        context("Composite Fairing") {
            val apiCF = CompositeFairing(
                height = Length(0.0, 0.0),
                diameter = Length(0.0, 0.0)
            )

            val dbCf = apiCF.toDbCompositeFairing()

            it("Should map values correctly") {
                dbCf.height shouldBe apiCF.height.toDbLength()
                dbCf.diameter shouldBe apiCF.diameter.toDbLength()
            }
        }

        context("Payloads") {
            val apiPayloads = Payloads(
                option1 = "Option 1",
                option2 = null,
                compositeFairing = CompositeFairing(Length(0.0, 0.0), Length(0.0, 0.0))
            )

            val dbPayloads = apiPayloads.toDbPayloads()

            it("Should map values correctly") {
                dbPayloads.option1 shouldBe apiPayloads.option1
                dbPayloads.option2 shouldBe apiPayloads.option2
                dbPayloads.compositeFairing shouldBe apiPayloads.compositeFairing.toDbCompositeFairing()
            }
        }

        context("Second Stage") {

            val apiSs = SecondStage(
                engines = 0,
                fuelAmountTons = 0.0,
                burnTimeSecs = 0.0,
                thrust = Thrust(0.0, 0.0),
                payloads = Payloads(
                    null,
                    null,
                    CompositeFairing(
                        Length(0.0, 0.0), Length(0.0, 0.0)
                    )
                )
            )

            val dbSs = apiSs.toDbSecondStage()

            it("Should map values correctly") {

                with(dbSs) {
                    engines shouldBe apiSs.engines
                    fuelAmountTons shouldBe apiSs.fuelAmountTons
                    burnTimeSec shouldBe apiSs.burnTimeSecs
                    thrust shouldBe apiSs.thrust.toDbThrust()
                    payloads shouldBe apiSs.payloads.toDbPayloads()
                }
            }
        }

        context("First Stage") {

            val apiFs = FirstStage(
                reusable = false,
                engines = 0,
                fuelAmountTons = 0.0,
                burnTimeSecs = 0.0,
                thrustVacuum = Thrust(0.0, 0.0),
                thrustSeaLevel = Thrust(0.0, 0.0)
            )

            val dbFs = apiFs.toDbFirstStage()

            it("Should map values correctly") {

                with(dbFs) {
                    reusable shouldBe apiFs.reusable
                    engines shouldBe apiFs.engines
                    fuelAmountTons shouldBe apiFs.fuelAmountTons
                    burnTimeSec shouldBe apiFs.burnTimeSecs
                    thrustVacuum shouldBe apiFs.thrustVacuum.toDbThrust()
                    thrustSeaLevel shouldBe apiFs.thrustSeaLevel.toDbThrust()
                }
            }
        }

        context("Payload Weight") {

            val apiPw = PayloadWeight(
                id = "ID",
                name = "Name",
                weightKg = 0.0,
                weightLb = 0.0
            )

            val rocketId = "ID"

            val dbPw = apiPw.toDbPayloadWeight(rocketId)

            it("Should map values correctly") {
                with(dbPw) {
                    payloadWeightId shouldBe apiPw.id
                    name shouldBe apiPw.name
                    kg shouldBe apiPw.weightKg
                    lb shouldBe apiPw.weightLb
                    rocketId shouldBe rocketId
                }
            }
        }

        context("Rocket") {
            val apiRocket = Rocket(
                id = 0,
                rockedId = "Rocket",
                rocketName = "Rocket",
                rocketType = "Type",
                active = false,
                stages = 0,
                boosters = 0,
                costPerLaunch = 0,
                successRate = 0.0,
                firstFlight = "01-01-1970",
                country = "Country",
                company = "Company",
                height = Length(0.0, 0.0),
                diameter = Length(0.0, 0.0),
                mass = Mass(0.0, 0.0),
                firstStage = FirstStage(false, 0, 0.0, 0.0, Thrust(0.0, 0.0), Thrust(0.0, 0.0)),
                secondStage = SecondStage(
                    0,
                    0.0,
                    0.0,
                    Thrust(0.0, 0.0),
                    Payloads(null, null, CompositeFairing(Length(0.0, 0.0), Length(0.0, 0.0)))
                ),
                engines = Engines(
                    0,
                    "Type",
                    "Version",
                    "Layout",
                    0,
                    "1",
                    "2",
                    Thrust(0.0, 0.0),
                    Thrust(0.0, 0.0),
                    0.0
                ),
                landingLegs = LandingLegs(
                    0,
                    "Material"
                ),
                wikipedia = "www.wikipedia.com/TestRocket",
                description = "Test Rocket",
                payloadWeights = listOf()
            )

            val dbRocket = apiRocket.toDbRocket()

            it("Should map values correctly") {
                with(dbRocket) {

                    rocketId shouldBe apiRocket.rockedId
                    rocketName shouldBe apiRocket.rocketName
                    id shouldBe apiRocket.id
                    active shouldBe apiRocket.active
                    stages shouldBe apiRocket.stages
                    boosters shouldBe apiRocket.boosters
                    costPerLaunch shouldBe apiRocket.costPerLaunch
                    successRatePercentage shouldBe apiRocket.successRate
                    firstFlight shouldBe apiRocket.firstFlight
                    country shouldBe apiRocket.country
                    company shouldBe apiRocket.company
                    height shouldBe apiRocket.height.toDbLength()
                    diameter shouldBe apiRocket.diameter.toDbLength()
                    mass shouldBe apiRocket.mass.toDbMass()
                    firstStage shouldBe apiRocket.firstStage.toDbFirstStage()
                    secondStage shouldBe apiRocket.secondStage.toDbSecondStage()
                    engines shouldBe apiRocket.engines.toDbEngine()
                    landingLegs shouldBe apiRocket.landingLegs.toDbLandingLegs()
                    wikipedia shouldBe apiRocket.wikipedia
                    description shouldBe apiRocket.description
                }
            }
        }
    }
})
