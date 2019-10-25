package com.haroldadmin.spacex_api_wrapper

import com.haroldadmin.spacex_api_wrapper.common.Length
import com.haroldadmin.spacex_api_wrapper.common.Location
import com.haroldadmin.spacex_api_wrapper.common.Mass
import com.haroldadmin.spacex_api_wrapper.common.OrbitParams
import com.haroldadmin.spacex_api_wrapper.common.Thrust
import com.haroldadmin.spacex_api_wrapper.launches.CoreSummary
import com.haroldadmin.spacex_api_wrapper.launches.Fairing
import com.haroldadmin.spacex_api_wrapper.launches.FirstStageSummary
import com.haroldadmin.spacex_api_wrapper.launches.Launch
import com.haroldadmin.spacex_api_wrapper.launches.LaunchSite
import com.haroldadmin.spacex_api_wrapper.launches.Links
import com.haroldadmin.spacex_api_wrapper.launches.Payload
import com.haroldadmin.spacex_api_wrapper.launches.RocketSummary
import com.haroldadmin.spacex_api_wrapper.launches.SecondStageSummary
import com.haroldadmin.spacex_api_wrapper.launches.Telemetry
import com.haroldadmin.spacex_api_wrapper.launches.Timeline
import com.haroldadmin.spacex_api_wrapper.launchpad.LaunchPad
import com.haroldadmin.spacex_api_wrapper.rocket.CompositeFairing
import com.haroldadmin.spacex_api_wrapper.rocket.Engines
import com.haroldadmin.spacex_api_wrapper.rocket.FirstStage
import com.haroldadmin.spacex_api_wrapper.rocket.LandingLegs
import com.haroldadmin.spacex_api_wrapper.rocket.PayloadWeight
import com.haroldadmin.spacex_api_wrapper.rocket.Payloads
import com.haroldadmin.spacex_api_wrapper.rocket.Rocket
import com.haroldadmin.spacex_api_wrapper.rocket.SecondStage
import java.util.Date

object SampleApiData {

    object LaunchPads {
        fun one(
            id: Int = 6,
            siteId: String = "vafb_slc_4e",
            siteName: String = "Vandenberg Air Force Base Space Launch Complex 4E"
        ): LaunchPad {
            return LaunchPad(
                id = id,
                status = "active",
                location = Location(
                    name = "Vandenberg Air Force Base",
                    region = "California",
                    latitude = 34.632093,
                    longitude = -120.610829
                ),
                vehiclesLaunched = listOf("Falcon 9"),
                attemptedLaunches = 12,
                successfulLaunches = 12,
                wikipedia = "https://en.wikipedia.org/wiki/Vandenberg_AFB_Space_Launch_Complex_4",
                details = "SpaceX primary west coast launch pad for polar orbits and sun synchronous orbits, primarily used for Iridium. Also intended to be capable of launching Falcon Heavy.",
                siteId = siteId,
                siteNameLong = siteName
            )
        }

        fun many(
            idGenerator: (Int) -> Int = { it },
            siteIdGenerator: (Int) -> String = { it.toString() },
            siteNameGenerator: (Int) -> String = { it.toString() }
        ): Sequence<LaunchPad> {
            var count = 0
            return generateSequence {
                one(idGenerator(count), siteIdGenerator(count), siteNameGenerator(count)).also { count++ }
            }
        }
    }

    object Launches {

        fun one(
            flightNumber: Int = 65,
            rocketId: String = "falcon9",
            siteId: String = "ccafs_slc_40",
            isUpcoming: Boolean = false
        ): Launch {
            return Launch(
                flightNumber = flightNumber,
                missionName = "Telstar 19V",
                missionId = listOf("F4F83DE"),
                launchYear = "2018",
                launchDate = Date(),
                tentativeMaxPrecision = "hour",
                isTentative = true,
                tbd = false,
                launchWindow = 7200,
                rocket = RocketSummary(
                    rocketId = rocketId,
                    name = "Falcon 9",
                    type = "FT",
                    firstStage = FirstStageSummary(
                        cores = listOf(
                            CoreSummary(
                                serial = "B1047",
                                flight = 1,
                                block = 5,
                                gridfins = true,
                                legs = true,
                                reused = false,
                                landSuccess = true,
                                landingIntent = true,
                                landingType = "ASDS",
                                landingVehicle = "OCISLY"
                            )
                        )
                    ),
                    secondState = SecondStageSummary(
                        block = 5,
                        payloads = listOf(
                            Payload(
                                id = "Telstar 19V",
                                noradId = listOf(43562),
                                reused = false,
                                customers = listOf("Telesat"),
                                nationality = "Canada",
                                manufacturer = "SSL",
                                payloadType = "Satellite",
                                payloadMassKg = 7076.0,
                                payloadMassLbs = 15600.0,
                                orbit = "GTO",
                                orbitParams = OrbitParams(
                                    referenceSystem = "geocentric",
                                    regime = "geostationary",
                                    longitude = -65.0,
                                    semiMajorAxisKm = 42163.837,
                                    eccentricity = 0.0001327,
                                    periapsisKm = 35780.107,
                                    apoapsisKm = 35791.297,
                                    inclinationDeg = 0.0126,
                                    periodMin = 1436.051,
                                    lifespanYears = 15.0,
                                    epoch = Date(),
                                    raan = 130.2989,
                                    meanMotion = 1.00274977,
                                    argOfPericenter = 165.1069,
                                    meanAnomaly = 64.5495
                                )
                            )
                        )
                    ),
                    fairing = Fairing(
                        reused = false,
                        recoveryAttempt = false,
                        recovered = false,
                        ship = null
                    )
                ),
                ships = listOf("GOPURSUIT", "GOQUEST", "HAWK", "OCISLY"),
                telemetry = Telemetry("https://www.flightclub.io/results/?code=TS19V"),
                launchSite = LaunchSite(
                    id = siteId,
                    name = "CCAFS SLC 40",
                    nameLong = "Cape Canaveral Air Force Station Space Launch Complex 40"
                ),
                launchSuccess = true,
                links = Links(
                    missionPatch = "https://images2.imgbox.com/c5/53/5jklZkPz_o.png",
                    missionPatchSmall = "https://images2.imgbox.com/12/7c/NiniYxoh_o.png",
                    redditCampaign = "https://www.reddit.com/r/spacex/comments/8w19yg/telstar_19v_launch_campaign_thread/",
                    redditLaunch = "https://www.reddit.com/r/spacex/comments/90p1a6/rspacex_telstar_19v_official_launch_discussion/",
                    redditRecovery = null,
                    redditMedia = "https://www.reddit.com/r/spacex/comments/90oxrr/rspacex_telstar_19v_media_thread_videos_images/",
                    pressKit = "http://www.spacex.com/sites/spacex/files/telstar19vantagepresskit.pdf",
                    article = "https://spaceflightnow.com/2018/07/22/spacex-delivers-for-telesat-with-successful-early-morning-launch/",
                    wikipedia = "https://en.wikipedia.org/wiki/Telstar_19V",
                    video = "https://www.youtube.com/watch?v=xybp6zLaGx4",
                    youtubeKey = "xybp6zLaGx4",
                    flickrImages = listOf(
                        "https://farm1.staticflickr.com/856/28684550147_49802752b3_o.jpg",
                        "https://farm1.staticflickr.com/927/28684552447_956a9744f1_o.jpg",
                        "https://farm2.staticflickr.com/1828/29700007298_8ac5891d2c_o.jpg",
                        "https://farm1.staticflickr.com/914/29700004918_31ed7b73ef_o.jpg",
                        "https://farm1.staticflickr.com/844/29700002748_3047e50a0a_o.jpg",
                        "https://farm2.staticflickr.com/1786/29700000688_2514cd3cbb_o.jpg"
                    )
                ),
                details = "SSL-manufactured communications satellite intended to be placed at 63° West over the Americas. At 7,075 kg, it became the heaviest commercial communications satellite ever launched.",
                upcoming = isUpcoming,
                staticFireDate = Date(),
                timeline = Timeline(
                    webcastLiftoff = 899,
                    goForPropLoading = -2280,
                    rp1Loading = -2100,
                    stage1LoxLoading = -2100,
                    stage2LoxLoading = -960,
                    engineChill = -420,
                    prelaunchChecks = -60,
                    propellantPressurization = -60,
                    goForLaunch = -45,
                    ignition = -3,
                    liftoff = 0,
                    maxQ = 72,
                    meco = 150,
                    stageSeparation = 153,
                    secondStateIgnition = 154,
                    fairingDeploy = 220,
                    firstStageEntryBurn = 372,
                    seco1 = 492,
                    fistStageLanding = 509,
                    secondStageRestart = 1609,
                    seco2 = 1659,
                    payloadDeploy = 1960
                )
            )
        }

        fun all(
            flightNumberGenerator: (Int) -> Int = { it },
            rocketIdGenerator: (Int) -> String = { it.toString() },
            siteIdGenerator: (Int) -> String = { it.toString() },
            isUpcomingGenerator: (Int) -> Boolean = { false }
        ): Sequence<Launch> {
            var count = 0
            return generateSequence {
                one(
                    flightNumberGenerator(count),
                    rocketIdGenerator(count),
                    siteIdGenerator(count),
                    isUpcomingGenerator(count)
                ).also { count++ }
            }
        }
    }

    object Rockets {

        fun one(id: Int = 2, rocketId: String = "falcon9"): Rocket {
            return Rocket(
                id = id,
                active = true,
                stages = 2,
                boosters = 0,
                costPerLaunch = 50000000,
                successRate = 97.0,
                firstFlight = "2010-06-04",
                country = "United States",
                company = "SpaceX",
                height = Length(
                    meters = 70.0,
                    feet = 12.0
                ),
                diameter = Length(
                    meters = 3.7,
                    feet = 12.0
                ),
                mass = Mass(
                    kg = 549054.0,
                    lb = 1207920.0
                ),
                payloadWeights = listOf(
                    PayloadWeight(
                        id = "leo",
                        name = "Low Earth Orbit",
                        weightKg = 22800.0,
                        weightLb = 50265.0
                    )
                ),
                firstStage = FirstStage(
                    reusable = true,
                    engines = 9,
                    fuelAmountTons = 385.0,
                    burnTimeSecs = 162.0,
                    thrustSeaLevel = Thrust(
                        kN = 7607.0,
                        lbf = 1710000.0
                    ),
                    thrustVacuum = Thrust(
                        kN = 8227.0,
                        lbf = 1849500.0
                    )
                ),
                secondStage = SecondStage(
                    engines = 1,
                    fuelAmountTons = 90.0,
                    burnTimeSecs = 397.0,
                    thrust = Thrust(
                        kN = 934.0,
                        lbf = 21000.0
                    ),
                    payloads = Payloads(
                        option1 = "dragon",
                        option2 = "composite_fairing",
                        compositeFairing = CompositeFairing(
                            height = Length(
                                meters = 13.1,
                                feet = 43.0
                            ),
                            diameter = Length(
                                meters = 5.2,
                                feet = 17.1
                            )
                        )
                    )
                ),
                engines = Engines(
                    number = 9,
                    type = "merlin",
                    version = "1D+",
                    layout = "octaweb",
                    engineLossMax = 2,
                    propellant1 = "liquid oxygen",
                    propellant2 = "RP-1 kerosene",
                    thrustSeaLevel = Thrust(
                        kN = 845.0,
                        lbf = 19000.0
                    ),
                    thrustVacuum = Thrust(
                        kN = 914.0,
                        lbf = 205500.0
                    ),
                    thrustToWeightRatio = 180.1
                ),
                landingLegs = LandingLegs(
                    number = 4,
                    material = "carbon fiber"
                ),
                wikipedia = "https://en.wikipedia.org/wiki/Falcon_9",
                description = "Falcon 9 is a two-stage rocket designed and manufactured by SpaceX for the reliable and safe transport of satellites and the Dragon spacecraft into orbit.",
                rocketId = rocketId,
                rocketName = "Falcon 9",
                rocketType = "rocket"
            )
        }

        fun all(
            idGenerator: (Int) -> Int = { it },
            rocketIdGenerator: (Int) -> String = { it.toString() }
        ): Sequence<Rocket> {
            var count = 0
            return generateSequence {
                one(idGenerator(count), rocketIdGenerator(count)).also { count++ }
            }
        }
    }

}