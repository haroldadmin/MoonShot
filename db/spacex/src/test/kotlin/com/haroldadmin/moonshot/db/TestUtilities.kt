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
        )
    )

    return database to { driver.close() }
}
