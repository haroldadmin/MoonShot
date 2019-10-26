package com.haroldadmin.moonshot.database

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {

    single<MoonShotDb> {
        Room.databaseBuilder<MoonShotDb>(
            androidContext(),
            MoonShotDb::class.java,
            "moonshot-db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    single<LaunchDao> {
        get<MoonShotDb>().launchDao()
    }

    single<RocketsDao> {
        get<MoonShotDb>().rocketsDao()
    }

    single<LaunchPadDao> {
        get<MoonShotDb>().launchPadDao()
    }
}