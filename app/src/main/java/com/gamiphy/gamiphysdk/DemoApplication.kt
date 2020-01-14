package com.gamiphy.gamiphysdk

import android.app.Application
import android.util.Log
import com.gamiphy.library.GamiBot
import com.gamiphy.library.OnRedeemTrigger
import com.gamiphy.library.network.models.responses.redeem.Redeem

class DemoApplication() : Application() {

    override fun onCreate() {
        super.onCreate()
        GamiBot.getInstance().init(applicationContext, "5dc9335e5d2ed200121fc720", "es").setDebug(true)

        GamiBot.getInstance().registerGamiphyOnRedeemTrigger(object : OnRedeemTrigger {
            override fun onRedeemTrigger(redeem: Redeem?) {
                Log.d(MainActivity::class.java.simpleName, "here is package Id  ${redeem?.packageId} ----- ${redeem?.pointsToRedeem}")
                GamiBot.getInstance()
                    .markRedeemDone(redeem?.packageId!!, redeem.pointsToRedeem!!)
            }
        })

    }
}