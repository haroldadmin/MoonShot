package com.haroldadmin.moonshot.models

import com.haroldadmin.moonshot.models.common.Length
import com.haroldadmin.moonshot.models.common.Location
import com.haroldadmin.moonshot.models.common.Mass
import com.haroldadmin.moonshot.models.common.Thrust
import com.haroldadmin.moonshot.models.launch.Fairings
import com.haroldadmin.moonshot.models.launch.Launch
import com.haroldadmin.moonshot.models.launch.LaunchSite
import com.haroldadmin.moonshot.models.launch.Links
import com.haroldadmin.moonshot.models.launch.Rocket as LaunchRocket
import com.haroldadmin.moonshot.models.launch.Telemetry
import com.haroldadmin.moonshot.models.launch.Timeline
import org.joda.time.format.ISODateTimeFormat
import java.util.Date

object SampleDbData {

    private val parser = ISODateTimeFormat.dateTime()

    object Launches {

        /**
         * Returns a Launch with some sample data
         */
        fun one(
            flightNumber: Int = 65,
            missionName: String = "Telstar 19V",
            siteId: String = "ccafs_slc_40",
            isUpcoming: Boolean = false,
            rocketId: String = "falcon9",
            launchDate: Date = parser.parseDateTime("2018-07-22T05:50:00.000Z").toDate()
        ): Launch {
            return Launch(
                flightNumber = flightNumber,
                missionName = missionName,
                missionId = listOf("F4F83DE"),
                launchYear = "2018",
                launchDateUtc = launchDate,
                isTentative = false,
                tentativeMaxPrecision = DatePrecision.hour,
                tbd = false,
                launchWindow = 7200,
                rocket = LaunchRocket(
                    rocketId = rocketId,
                    rocketName = "Falcon 9",
                    rocketType = "FT",
                    fairings = Fairings(
                        reused = false,
                        recoveryAttempt = false,
                        recovered = false,
                        ship = null
                    )
                ),
                ships = listOf("GOPURSUIT", "GOQUEST", "HAWK", "OCISLY"),
                telemetry = Telemetry("https://www.flightclub.io/results/?code=TS19V"),
                launchSite = LaunchSite(
                    siteId = siteId,
                    siteName = "CCAFS SLC 40",
                    siteNameLong = "Cape Canaveral Air Force Station Space Launch Complex 40"
                ),
                launchSuccess = true,
                links = Links(
                    missionPatch = "https://images2.imgbox.com/c5/53/5jklZkPz_o.png",
                    missionPatchSmall = "https://images2.imgbox.com/12/7c/NiniYxoh_o.png",
                    redditCampaign = "https://www.reddit.com/r/spacex/comments/8w19yg/telstar_19v_launch_campaign_thread/",
                    redditLaunch = "https://www.reddit.com/r/spacex/comments/90p1a6/rspacex_telstar_19v_official_launch_discussion/",
                    redditRecovery = null,
                    redditMedia = "https://www.reddit.com/r/spacex/comments/90oxrr/rspacex_telstar_19v_media_thread_videos_images/",
                    presskit = "http://www.spacex.com/sites/spacex/files/telstar19vantagepresskit.pdf",
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
                isUpcoming = isUpcoming,
                staticFireDateUtc = parser.parseDateTime("2018-07-18T21:00:00.000Z").toDate(),
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
                    maxq = 72,
                    meco = 150,
                    stageSeparation = 153,
                    secondStageIgnition = 154,
                    fairingDeploy = 220,
                    firstStageEntryBurn = 372,
                    seco1 = 492,
                    firstStageLanding = 509,
                    secondStageRestart = 1609,
                    seco2 = 1659,
                    payloadDeploy = 1960
                )
            )
        }

        /**
         * Returns a list of launches with sample data.
         *
         * The caller has control over how FlightIDs and SiteIDs are generated, because they can
         * supply producers for these values.
         */
        fun many(
            flightIdGenerator: (Int) -> Int = { it },
            missionNameGenerator: (Int) -> String = { it.toString() },
            siteIdGenerator: (Int) -> String = { it.toString() },
            isUpcomingGenerator: (Int) -> Boolean = { false },
            rocketIdGenerator: (Int) -> String = { it.toString() },
            launchDateGenerator: (Int) -> Date = { Date(it.toLong()) }
        ): Sequence<Launch> {
            var counter = 0
            return generateSequence {
                one(
                    flightIdGenerator(counter),
                    missionNameGenerator(counter),
                    siteIdGenerator(counter),
                    isUpcomingGenerator(counter),
                    rocketIdGenerator(counter),
                    launchDateGenerator(counter)
                ).also { counter++ }
            }
        }
    }

    object Rockets {
        fun one(id: Long = 2, rocketId: String = "falcon9", rocketName: String = "Falcon 9"): Rocket {
            return Rocket(
                id = id,
                active = true,
                stages = 2,
                boosters = 0,
                costPerLaunch = 50000000,
                successRatePercentage = 97.0,
                firstFlight = "2010-06-04",
                country = "United States",
                company = "SpaceX",
                height = Length(
                    metres = 70.0,
                    feet = 12.0
                ),
                diameter = Length(
                    metres = 3.7,
                    feet = 12.0
                ),
                mass = Mass(
                    kg = 549054.0,
                    lb = 1207920.0
                ),
                firstStage = FirstStage(
                    reusable = true,
                    engines = 9,
                    fuelAmountTons = 385.0,
                    burnTimeSec = 162.0,
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
                    burnTimeSec = 397.0,
                    thrust = Thrust(
                        kN = 934.0,
                        lbf = 21000.0
                    ),
                    payloads = Payloads(
                        option1 = "dragon",
                        option2 = "composite_fairing",
                        compositeFairing = CompositeFairing(
                            height = Length(
                                metres = 13.1,
                                feet = 43.0
                            ),
                            diameter = Length(
                                metres = 5.2,
                                feet = 17.1
                            )
                        )
                    )
                ),
                engines = Engine(
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
                    thrustToWeight = 180.1
                ),
                landingLegs = LandingLegs(
                    number = 4,
                    material = "carbon fiber"
                ),
                wikipedia = "https://en.wikipedia.org/wiki/Falcon_9",
                description = "Falcon 9 is a two-stage rocket designed and manufactured by SpaceX for the reliable and safe transport of satellites and the Dragon spacecraft into orbit.",
                rocketId = rocketId,
                rocketName = rocketName,
                rocketType = "rocket"
            )
        }

        fun many(
            idGenerator: (Int) -> Long = { it.toLong() },
            rocketIdGenerator: (Int) -> String = { it.toString() },
            rocketNameGenerator: (Int) -> String = { it.toString() }
        ): Sequence<Rocket> {
            var counter = 0
            return generateSequence {
                one(idGenerator(counter), rocketIdGenerator(counter), rocketNameGenerator(counter)).also { counter++ }
            }
        }
    }

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
                vehiclesLanded = listOf("Falcon 9"),
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

    object Missions {
        fun one(id: String = "F3364BF"): Mission {
            return Mission(
                name = "Iridium NEXT",
                id = id,
                manufacturers = listOf("Orbital ATK"),
                payloadIds = listOf(
                    "Iridium NEXT 1",
                    "Iridium NEXT 2",
                    "Iridium NEXT 3",
                    "Iridium NEXT 4",
                    "Iridium NEXT 5",
                    "Iridium NEXT 6",
                    "Iridium NEXT 7"
                ),
                wikipedia = "https://en.wikipedia.org/wiki/Iridium_satellite_constellation",
                website = "https://www.iridiumnext.com/",
                twitter = "https://twitter.com/IridiumBoss?lang=en",
                description = "In 2017, Iridium began launching Iridium NEXT, a second-generation worldwide network of telecommunications satellites, consisting of 66 active satellites, with another nine in-orbit spares and six on-ground spares. These satellites will incorporate features such as data transmission that were not emphasized in the original design. The constellation will provide L-band data speeds of up to 128 kbit/s to mobile terminals, up to 1.5 Mbit/s to Iridium Pilot marine terminals, and high-speed Ka-band service of up to 8 Mbit/s to fixed/transportable terminals. The next-generation terminals and service are expected to be commercially available by the end of 2018. However, Iridium's proposed use of its next-generation satellites has raised concerns the service will harmfully interfere with GPS devices. The satellites will incorporate a secondary payload for Aireon, a space-qualified ADS-B data receiver. This is for use by air traffic control and, via FlightAware, for use by airlines. A tertiary payload on 58 satellites is a marine AIS ship-tracker receiver, for Canadian company exactEarth Ltd. Iridium can also be used to provide a data link to other satellites in space, enabling command and control of other space assets regardless of the position of ground stations and gateways."
            )
        }

        fun many(
            idGenerator: (Int) -> String = { it.toString() }
        ): Sequence<Mission> {
            var count = 0
            return generateSequence {
                one(id = idGenerator(count)).also { count++ }
            }
        }
    }
}