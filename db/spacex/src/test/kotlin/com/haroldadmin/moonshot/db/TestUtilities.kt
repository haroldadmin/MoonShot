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
            launchIDsAdapter = LaunchIDAdapter()
        )
    )

    return database to { driver.close() }
}
