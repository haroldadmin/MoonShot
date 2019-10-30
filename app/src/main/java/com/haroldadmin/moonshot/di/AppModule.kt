package com.haroldadmin.moonshot.di

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.squareup.inject.assisted.dagger2.AssistedModule
import dagger.Module
import dagger.Provides
import javax.inject.Named

@AssistedModule
@Module(includes = [AssistedInject_AppModule::class])
object AppModule {
    @Provides
    @Named("settings")
    fun settings(context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }
}
