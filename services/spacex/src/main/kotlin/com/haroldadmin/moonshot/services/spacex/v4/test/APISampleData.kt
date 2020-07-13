package com.haroldadmin.moonshot.services.spacex.v4.test

import com.haroldadmin.moonshot.services.spacex.v4.*
import java.time.LocalDate

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
}