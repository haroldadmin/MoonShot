package com.haroldadmin.moonshot.di

import android.content.Context
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.haroldadmin.moonshot.db.*
import com.haroldadmin.moonshot.db.spacex.SpacexDatabase
import com.squareup.sqldelight.EnumColumnAdapter
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object DatabaseModule {

    @Provides
    @Singleton
    fun database(context: Context): SpacexDatabase {
        val driver = AndroidSqliteDriver(SpacexDatabase.Schema, context, "spacex.db")
        return SpacexDatabase(
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
            ),
            payloadAdapter = Payload.Adapter(
                customersAdapter = ListToStringAdapter(),
                manufacturersAdapter = ListToStringAdapter(),
                nationalitiesAdapter = ListToStringAdapter(),
                epochAdapter = ZonedDateTimeAdapter(),
                noradIDsAdapter = ListToIntAdapter()
            ),
            roadsterInfoAdapter = RoadsterInfo.Adapter(
                flickrImagesAdapter = ListToStringAdapter(),
                launchDateUTCAdapter = ZonedDateTimeAdapter()
            ),
            rocketAdapter = Rocket.Adapter(
                flickrImagesAdapter = ListToStringAdapter()
            ),
            shipAdapter = Ship.Adapter(
                launchIDsAdapter = ListToStringAdapter(),
                rolesAdapter = ListToStringAdapter()
            )
        )
    }

    @Provides
    @Singleton
    fun launchQueries(db: SpacexDatabase): LaunchQueries {
        return db.launchQueries
    }

}