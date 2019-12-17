package com.gamiphy.gamiphysdk

import android.app.Application
import com.gamiphy.library.GamiBot

class DemoApplication() : Application() {

    override fun onCreate() {
        super.onCreate()
        GamiBot.getInstance().init(applicationContext, "5dc9335e5d2ed200121fc720", "es").setDebug(true)
    }

}