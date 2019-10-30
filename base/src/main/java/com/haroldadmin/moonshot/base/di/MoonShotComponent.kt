package com.haroldadmin.moonshot.base.di

import android.app.Activity
import android.content.BroadcastReceiver
import androidx.fragment.app.Fragment

interface MoonShotComponent <T> {
    fun inject(target: T)
}

interface MoonShotActivityComponent <T : Activity> : MoonShotComponent<T>

interface MoonShotFragmentComponent <T : Fragment> : MoonShotComponent<T>

interface MoonShotBroadcastReceiverComponent <T : BroadcastReceiver> : MoonShotComponent<T>