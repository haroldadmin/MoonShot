package com.haroldadmin.moonshot.database

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val androidTestModule = module {
    single<MoonShotDb> {
        Room
            .inMemoryDatabaseBuilder(androidContext(), MoonShotDb::class.java)
            .allowMainThreadQueries()
            .build()
    }
}