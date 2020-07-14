package com.haroldadmin.moonshot.db

import com.haroldadmin.moonshot.db.spacex.SpacexDatabase
import com.squareup.sqldelight.EnumColumnAdapter
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver

internal fun useDatabase(): Pair<SpacexDatabase, () -> Unit> {
    val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)

    SpacexDatabase.Schema.create(driver)

    val database = SpacexDatabase(
        driver = driver,
        capsuleAdapter = Capsule.Adapter(
            statusAdapter = EnumColumnAdapter(),
            launchIDsAdapter = ListToStringAdapter()
        ),
        coreAdapter = Core.Adapter(
            statusAdapter = EnumColumnAdapter(),
            launchIDsAdapter = ListToStringAdapter()
        ),
        crewAdapter = Crew.Adapter(
            launchIDsAdapter = ListToStringAdapter(),
            statusAdapter = EnumColumnAdapter()
        ),
        dragonAdapter = Dragon.Adapter(
            flickrImagesAdapter = ListToStringAdapter(),
            firstFlightAdapter = LocalDateAdapter()
        ),
        landingPadAdapter = LandingPad.Adapter(
            launchIDsAdapter = ListToStringAdapter()
        ),
        launchAdapter = Launch.Adapter(
            capsuleIDsAdapter = ListToStringAdapter(),
            crewIDsAdapter = ListToStringAdapter(),
            datePrecisionAdapter = EnumColumnAdapter(),
            failuresAdapter = ListToStringAdapter(),
            fairings_shipIDsAdapter = ListToStringAdapter(),
            links_flickr_originalAdapter = ListToStringAdapter(),
            links_flickr_smallAdapter = ListToStringAdapter(),
            payloadIDsAdapter = ListToStringAdapter(),
            shipIDsAdapter = ListToStringAdapter(),
            launchDateLocalAdapter = ZonedDateTimeAdapter(),
            launchDateUTCAdapter = ZonedDateTimeAdapter(),
            staticFireDateUTCAdapter = ZonedDateTimeAdapter()
        ),
        launchPadAdapter = LaunchPad.Adapter(
            launchIDsAdapter = ListToStringAdapter(),
            rocketIDsAdapter = ListToStringAdapter()
        )
    )

    return database to { driver.close() }
}
