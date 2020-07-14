package com.haroldadmin.moonshot.services.spacex.v4.test

import com.haroldadmin.moonshot.services.spacex.v4.Capsule
import com.haroldadmin.moonshot.services.spacex.v4.CompanyInfo
import com.haroldadmin.moonshot.services.spacex.v4.Core
import com.haroldadmin.moonshot.services.spacex.v4.CoreStatus
import com.haroldadmin.moonshot.services.spacex.v4.Crew
import com.haroldadmin.moonshot.services.spacex.v4.CrewStatus
import com.haroldadmin.moonshot.services.spacex.v4.DatePrecision
import com.haroldadmin.moonshot.services.spacex.v4.Dragon
import com.haroldadmin.moonshot.services.spacex.v4.LandingPad
import com.haroldadmin.moonshot.services.spacex.v4.Launch
import com.haroldadmin.moonshot.services.spacex.v4.LaunchPad
import com.haroldadmin.moonshot.services.spacex.v4.Payload
import java.time.LocalDate
import java.time.ZonedDateTime

object APISampleData {

    object Capsules {
        fun samples(): Sequence<Capsule> {
            return generateSequence {
                Capsule(
                    reuseCount = 1,
                    waterLandings = 1,
                    landLandings = 0,
                    lastUpdate = "Reentered after three weeks in orbit",
                    launchIDs = listOf("5eb87cdeffd86e000604b330"),
                    serial = "C101",
                    status = Capsule.CapsuleStatus.retired,
                    id = "5e9e2c5bf35918ed873b2664",
                    dragon = null
                )
            }
        }
    }

    object Company {
        fun samples(): Sequence<CompanyInfo> {
            return generateSequence {
                CompanyInfo(
                    headquarters = CompanyInfo.Headquarters(
                        address = "Rocket Road",
                        city = "Hawthorne",
                        state = "California"
                    ),
                    links = CompanyInfo.Links(
                        website = "https://www.spacex.com/",
                        flickr = "https://www.flickr.com/photos/spacex/",
                        twitter = "https://twitter.com/SpaceX",
                        elonTwitter = "https://twitter.com/elonmusk"
                    ),
                    name = "SpaceX",
                    founder = "Elon Musk",
                    founded = 2002,
                    employees = 8000,
                    vehicles = 3,
                    launchSites = 3,
                    testSites = 1,
                    ceo = "Elon Musk",
                    cto = "Elon Musk",
                    coo = "Gwynne Shotwell",
                    ctoPropulsion = "Tom Mueller",
                    valuation = 52000000000.0,
                    summary = "SpaceX designs, manufactures and launches advanced rockets and spacecraft. The company was founded in 2002 to revolutionize space technology, with the ultimate goal of enabling people to live on other planets.",
                    id = "5eb75edc42fea42237d7f3ed"
                )
            }
        }
    }

    object Cores {
        fun samples(): Sequence<Core> {
            return generateSequence {
                Core(
                    block = 5,
                    reuseCount = 3,
                    rtlsAttempts = 1,
                    rtlsLandings = 1,
                    asdsAttempts = 3,
                    asdsLandings = 3,
                    lastUpdate = "Landed on OCISLY as of Jan 29, 2020. ",
                    launchIDs = listOf(
                        "5eb87d2bffd86e000604b375",
                        "5eb87d31ffd86e000604b379",
                        "5eb87d3fffd86e000604b382",
                        "5eb87d44ffd86e000604b386"
                    ),
                    serial = "B1051",
                    status = CoreStatus.active,
                    id = "5e9e28a6f35918c0803b265c"
                )
            }
        }
    }

    object Crews {
        fun samples(): Sequence<Crew> {
            return generateSequence {
                Crew(
                    name = "Douglas Hurley",
                    agency = "NASA",
                    image = "https://i.imgur.com/ooaayWf.png",
                    wikipedia = "https://en.wikipedia.org/wiki/Douglas_G._Hurley",
                    launchIDs = listOf("5eb87d46ffd86e000604b388"),
                    status = CrewStatus.active,
                    id = "5ebf1b7323a9a60006e03a7b"
                )
            }
        }
    }

    object Dragons {
        fun samples(): Sequence<Dragon> {
            return generateSequence {
                Dragon(
                    heatShield = Dragon.HeatShield(
                        material = "PICA-X",
                        sizeMetre = 3.6,
                        tempDegree = 3000.0,
                        devPartner = "NASA"
                    ),
                    launchPayloadMass = Dragon.Mass(
                        kg = 6000.0,
                        lb = 13228.0
                    ),
                    launchPayloadVolume = Dragon.Volume(
                        cubicMetres = 25.0,
                        cubicFeet = 883.0
                    ),
                    returnPayloadMass = Dragon.Mass(
                        kg = 3000.0,
                        lb = 6614.0
                    ),
                    returnPayloadVolume = Dragon.Volume(
                        cubicMetres = 11.0,
                        cubicFeet = 388.0
                    ),
                    pressurizedCapsule = Dragon.PressurizedCapsule(
                        payloadVolume = Dragon.Volume(
                            cubicMetres = 11.0,
                            cubicFeet = 388.0
                        )
                    ),
                    trunk = Dragon.Trunk(
                        trunkVolume = Dragon.Volume(
                            cubicMetres = 14.0,
                            cubicFeet = 494.0
                        ),
                        cargo = Dragon.Trunk.Cargo(
                            solarArray = 2,
                            unpressurizedCargo = true
                        )
                    ),
                    heightWithTrunk = Dragon.Length(
                        metres = 7.2,
                        feet = 23.6
                    ),
                    diameter = Dragon.Length(
                        metres = 3.7,
                        feet = 12.0
                    ),
                    firstFlight = LocalDate.parse("2010-12-08"),
                    flickrImages = listOf(
                        "https://www.spacex.com/sites/spacex/files/styles/media_gallery_large/public/2015_-_04_crs5_dragon_orbit13.jpg?itok=9p8_l7UP",
                        "https://www.spacex.com/sites/spacex/files/styles/media_gallery_large/public/2012_-_4_dragon_grapple_cots2-1.jpg?itok=R2-SeuMX",
                        "https://farm3.staticflickr.com/2815/32761844973_4b55b27d3c_b.jpg",
                        "https://farm9.staticflickr.com/8618/16649075267_d18cbb4342_b.jpg"
                    ),
                    name = "Dragon 1",
                    type = "capsule",
                    active = true,
                    crewCapacity = 0,
                    sidewallAngleDegrees = 15.0,
                    orbitDurationYears = 2.0,
                    dryMassKg = 4200.0,
                    dryMassLb = 9300.0,
                    thrusters = listOf(
                        Dragon.Thruster(
                            type = "Draco",
                            amount = 18,
                            pods = 4,
                            fuelOne = "nitrogen tetroxide",
                            fuelTwo = "monomethylhydrazine",
                            isp = 300,
                            thrust = Dragon.Thrust(
                                kN = 0.4,
                                lbf = 90.0
                            )
                        )
                    ),
                    wikipedia = "https://en.wikipedia.org/wiki/SpaceX_Dragon",
                    description = "Dragon is a reusable spacecraft developed by SpaceX, an American private space transportation company based in Hawthorne, California. Dragon is launched into space by the SpaceX Falcon 9 two-stage-to-orbit launch vehicle. The Dragon spacecraft was originally designed for human travel, but so far has only been used to deliver cargo to the International Space Station (ISS).",
                    id = "5e9d058759b1ff74a7ad5f8f"
                )
            }
        }
    }

    object LandingPads {
        fun samples(): Sequence<LandingPad> {
            return generateSequence {
                LandingPad(
                    name = "LZ-2",
                    fullName = "Landing Zone 2",
                    status = "active",
                    type = "RTLS",
                    locality = "Cape Canaveral",
                    region = "Florida",
                    latitude = 28.485833,
                    longitude = -80.544444,
                    landingAttempts = 3,
                    landingSuccesses = 3,
                    wikipedia = "https://en.wikipedia.org/wiki/Landing_Zones_1_and_2",
                    details = "SpaceX's first east coast landing pad is Landing Zone 1, where the historic first Falcon 9 landing occurred in December 2015. LC-13 was originally used as a launch pad for early Atlas missiles and rockets from Lockheed Martin. LC-1 was later expanded to include Landing Zone 2 for side booster RTLS Falcon Heavy missions, and it was first used in February 2018 for that purpose.",
                    launchIDs = listOf(
                        "5eb87d13ffd86e000604b360",
                        "5eb87d2dffd86e000604b376",
                        "5eb87d35ffd86e000604b37a"
                    ),
                    id = "5e9e3032383ecb90a834e7c8"
                )
            }
        }
    }

    object Launches {
        fun samples(): Sequence<Launch> {
            return generateSequence {
                Launch(
                    fairings = null,
                    links = Launch.Links(
                        patch = Launch.Links.Patch(
                            small = "https://images2.imgbox.com/53/22/dh0XSLXO_o.png",
                            large = "https://images2.imgbox.com/15/2b/NAcsTEB6_o.png"
                        ),
                        reddit = Launch.Links.Reddit(
                            campaign = "https://www.reddit.com/r/spacex/comments/ezn6n0/crs20_launch_campaign_thread",
                            launch =
                            "https://www.reddit.com/r/spacex/comments/fe8pcj/rspacex_crs20_official_launch_discussion_updates/",
                            media =
                            "https://www.reddit.com/r/spacex/comments/fes64p/rspacex_crs20_media_thread_videos_images_gifs/",
                            recovery = null
                        ),
                        flickr = Launch.Links.Flickr(
                            small = emptyList(),
                            original = listOf(
                                "https://live.staticflickr.com/65535/49635401403_96f9c322dc_o.jpg",
                                "https://live.staticflickr.com/65535/49636202657_e81210a3ca_o.jpg",
                                "https://live.staticflickr.com/65535/49636202572_8831c5a917_o.jpg",
                                "https://live.staticflickr.com/65535/49635401423_e0bef3e82f_o.jpg",
                                "https://live.staticflickr.com/65535/49635985086_660be7062f_o.jpg"
                            )
                        ),
                        presskit = "https://www.spacex.com/sites/spacex/files/crs-20_mission_press_kit.pdf",
                        webcast = "https://youtu.be/1MkcWK2PnsU",
                        youtubeID = "1MkcWK2PnsU",
                        article =
                        "https://spaceflightnow.com/2020/03/07/late-night-launch-of-spacex-cargo-ship-marks-end-of-an-era/",
                        wikipedia = "https://en.wikipedia.org/wiki/SpaceX_CRS-20"
                    ),
                    staticFireDateUTC = ZonedDateTime.parse("2020-03-01T10:20:00.000Z"),
                    staticFireDateUnix = 1583058000,
                    tbd = false,
                    net = false,
                    window = 0,
                    rocketID = "5e9d0d95eda69973a809d1ec",
                    success = true,
                    failures = emptyList(),
                    details = "SpaceX's 20th and final Crew Resupply Mission under the original NASA CRS contract, this mission brings essential supplies to the International Space Station using SpaceX's reusable Dragon spacecraft. It is the last scheduled flight of a Dragon 1 capsule. (CRS-21 and up under the new Commercial Resupply Services 2 contract will use Dragon 2.) The external payload for this mission is the Bartolomeo ISS external payload hosting platform. Falcon 9 and Dragon will launch from SLC-40, Cape Canaveral Air Force Station and the booster will land at LZ-1. The mission will be complete with return and recovery of the Dragon capsule and down cargo.",
                    crewIDs = emptyList(),
                    shipIDs = emptyList(),
                    capsuleIDs = listOf("5e9e2c5cf359185d753b266f"),
                    payloadIDs = listOf("5eb0e4d0b6c3bb0006eeb253"),
                    launchpadID = "5e9e4501f509094ba4566f84",
                    autoUpdate = true,
                    flightNumber = 91,
                    name = "CRS-20",
                    launchDateUTC = ZonedDateTime.parse("2020-03-07T04:50:31.000Z"),
                    launchDateUnix = 1583556631L,
                    launchDateLocal = ZonedDateTime.parse("2020-03-06T23:50:31-05:00"),
                    datePrecision = DatePrecision.hour,
                    upcoming = false,
                    cores = listOf(
                        Launch.Core(
                            id = "5e9e28a7f359187afd3b2662",
                            flight = 2,
                            gridfins = true,
                            legs = true,
                            reused = true,
                            landingAttempt = true,
                            landingSuccess = true,
                            landingType = "RTLS",
                            landpadID = "5e9e3032383ecb267a34e7c7"
                        )
                    ),
                    id = "5eb87d42ffd86e000604b384"
                )
            }
        }
    }

    object LaunchPads {
        fun samples(): Sequence<LaunchPad> {
            return generateSequence {
                LaunchPad(
                    name = "VAFB SLC 4E",
                    fullName = "Vandenberg Air Force Base Space Launch Complex 4E",
                    locality = "Vandenberg Air Force Base",
                    region = "California",
                    timezone = "America/Los_Angeles",
                    latitude = 34.632093,
                    longitude = -120.610829,
                    launchAttempts = 15,
                    launchSuccesses = 15,
                    rocketIDs = listOf("5e9d0d95eda69973a809d1ec"),
                    launchIDs = listOf(
                        "5eb87ce1ffd86e000604b334",
                        "5eb87cf0ffd86e000604b343",
                        "5eb87cfdffd86e000604b34c",
                        "5eb87d05ffd86e000604b354",
                        "5eb87d08ffd86e000604b357",
                        "5eb87d0affd86e000604b359",
                        "5eb87d0fffd86e000604b35d",
                        "5eb87d14ffd86e000604b361",
                        "5eb87d16ffd86e000604b363",
                        "5eb87d1affd86e000604b367",
                        "5eb87d1fffd86e000604b36b",
                        "5eb87d23ffd86e000604b36e",
                        "5eb87d25ffd86e000604b370",
                        "5eb87d28ffd86e000604b373",
                        "5eb87d31ffd86e000604b379"
                    ),
                    status = "active",
                    id = "5e9e4502f509092b78566f87"
                )
            }
        }
    }

    object Payloads {
        fun samples(): Sequence<Payload> {
            return generateSequence {
                Payload(
                    dragon = Payload.Dragon(
                        capsuleID = null,
                        massReturnedKg = null,
                        massReturnedLbs = null,
                        flightTimeSec = null,
                        manifest = null,
                        waterLanding = null,
                        landLanding = null
                    ),
                    name = "Tintin A & B",
                    type = "Satellite",
                    reused = false,
                    launchID = "5eb87d14ffd86e000604b361",
                    customers = listOf("SpaceX"),
                    noradIDs = listOf(43216, 43217),
                    nationalities = listOf("United States"),
                    manufacturers = listOf("SpaceX"),
                    massKg = 800.0,
                    massLbs = 1763.7,
                    orbit = "SSO",
                    referenceSystem = "geocentric",
                    regime = "low-earth",
                    longitude = null,
                    semiMajorAxisKm = 6737.42,
                    eccentricity = 0.0012995,
                    periapsisKm = 350.53,
                    apoapsisKm = 368.04,
                    inclinationDeg = 97.4444,
                    periodMin = 91.727,
                    lifespanYears = 1.0,
                    epoch = ZonedDateTime.parse("2020-06-13T13:46:31.000Z"),
                    meanMotion = 15.69864906,
                    raan = 176.6734,
                    argOfPericentre = 174.2326,
                    meanAnomaly = 185.9087,
                    id = "5eb0e4c6b6c3bb0006eeb21e"
                )
            }
        }
    }
}