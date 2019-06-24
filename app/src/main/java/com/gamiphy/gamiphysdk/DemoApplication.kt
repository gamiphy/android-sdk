package com.gamiphy.gamiphysdk

import android.app.Application
import com.gamiphy.library.GamiBot

class DemoApplication() : Application() {

    override fun onCreate() {
        super.onCreate()
        GamiBot.getInstance().init("5cedbbbb6801bb00173e5dfb").setDebug(true)
    }

}