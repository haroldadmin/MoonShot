package com.haroldadmin.moonshot.di

import android.content.Context
import android.content.SharedPreferences
import android.os.Handler
import android.os.HandlerThread
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

    @Provides
    @Named("epoxy-differ")
    fun epoxyDiffer(): Handler {
        val handlerThread = HandlerThread("epoxy-differ")
        handlerThread.start()
        return Handler(handlerThread.looper)
    }

    @Provides
    @Named("epoxy-builder")
    fun epoxyBuilder(): Handler {
        val handlerThread = HandlerThread("epoxy-differ")
        handlerThread.start()
        return Handler(handlerThread.looper)
    }
}
