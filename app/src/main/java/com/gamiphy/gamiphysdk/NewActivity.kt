package com.gamiphy.gamiphysdk

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class NewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new)
//        GamiBot.getInstance().registerGamiphyOnRedeemTrigger(object : OnRedeemTrigger {
//            override fun onRedeemTrigger(redeem: Redeem?) {
//                Log.d(
//                    MainActivity::class.java.simpleName,
//                    "here is package Id  ${redeem?.packageId} ----- ${redeem?.pointsToRedeem}"
//                )
//                GamiBot.getInstance()
//                    .markRedeemDone(redeem?.packageId!!, redeem.pointsToRedeem!!)
//            }
//        })

//        startActivity(MainActivity.newIntent(this))
    }


    companion object {
        fun newIntent(context: Context) = Intent(context, NewActivity::class.java)
    }
}
