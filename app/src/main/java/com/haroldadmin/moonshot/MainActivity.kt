package com.haroldadmin.moonshot

import android.os.Bundle
import com.haroldadmin.moonshot.base.MoonShotActivity

class MainActivity : MoonShotActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
