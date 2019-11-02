package com.haroldadmin.moonshot.database

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(context: Context): MoonShotDb {
        return Room.databaseBuilder(context, MoonShotDb::class.java, "moonshot-db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideLaunchDao(db: MoonShotDb): LaunchDao {
        return db.launchDao()
    }

    @Provides
    @Singleton
    fun provideRocketsDao(db: MoonShotDb): RocketsDao {
        return db.rocketsDao()
    }

    @Provides
    @Singleton
    fun provideLaunchpadDao(db: MoonShotDb): LaunchPadDao {
        return db.launchPadDao()
    }

    @Provides
    fun provideApplicationInfoDao(db: MoonShotDb): ApplicationInfoDao {
        return db.applicationInfoDao()
    }

    @Provides
    fun missionDao(db: MoonShotDb): MissionDao {
        return db.missionDao()
    }
}