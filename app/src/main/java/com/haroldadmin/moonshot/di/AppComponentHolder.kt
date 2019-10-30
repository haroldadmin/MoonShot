package com.haroldadmin.moonshot.di

object AppComponentHolder {
    internal lateinit var appComponent: AppComponent
        private set

    internal fun init(appComponent: AppComponent) {
        this.appComponent = appComponent
    }
}

fun appComponent(): AppComponent = AppComponentHolder.appComponent