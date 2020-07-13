package com.haroldadmin.moonshot.services.spacex.v4.test

import com.haroldadmin.moonshot.services.spacex.v4.Capsule
import com.haroldadmin.moonshot.services.spacex.v4.CompanyInfo

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
}