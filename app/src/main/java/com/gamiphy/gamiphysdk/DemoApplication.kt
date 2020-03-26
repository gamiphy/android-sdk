package com.gamiphy.gamiphysdk

import android.app.Application
import android.util.Log
import com.gamiphy.library.GamiBot
import com.gamiphy.library.OnRedeemTrigger
import com.gamiphy.library.network.models.responses.redeem.Redeem

class DemoApplication() : Application() {

    override fun onCreate() {
        super.onCreate()
        val botId="5e550cb17686f0001299e853"
        GamiBot.getInstance().init(this,botId,"en").setDebug(true)

        GamiBot.getInstance().registerGamiphyOnRedeemTrigger(object : OnRedeemTrigger {
            override fun onRedeemTrigger(redeem: Redeem?) {
                Log.d(MainActivity::class.java.simpleName, "here is package Id  ${redeem?.packageId} ----- ${redeem?.pointsToRedeem}")
                GamiBot.getInstance()
                    .markRedeemDone(redeem?.packageId!!, redeem.pointsToRedeem!!)
            }
        })

    }
}