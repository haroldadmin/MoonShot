package com.haroldadmin.moonshot.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import org.junit.rules.TestWatcher
import org.junit.runner.Description

class MoonShotDbRule: TestWatcher() {

    lateinit var database: MoonShotDb

    override fun starting(description: Description?) {
        super.starting(description)
        val context = ApplicationProvider.getApplicationContext<Context>()
        database =  Room.inMemoryDatabaseBuilder(context, MoonShotDb::class.java).build()
    }

    override fun finished(description: Description?) {
        super.finished(description)
        database.apply {
            clearAllTables()
            close()
        }
    }

    inline fun <reified T> dao(): Lazy<T> {
        return lazy {
            with(database) {
                when (T::class) {
                    LaunchDao::class -> launchDao() as T
                    RocketsDao::class -> rocketsDao() as T
                    LaunchPadDao::class -> launchPadDao() as T
                    else -> error("Unknown DAO requested")
                }
            }
        }
    }
}

