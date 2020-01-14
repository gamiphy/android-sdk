package com.gamiphy.gamiphysdk

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.gamiphy.library.GamiBot
import com.gamiphy.library.OnAuthTrigger
import com.gamiphy.library.OnRedeemTrigger
import com.gamiphy.library.OnTaskTrigger
import com.gamiphy.library.models.User
import com.gamiphy.library.network.models.responses.redeem.Redeem
import com.gamiphy.library.utils.HashUtil
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.bot)
        val gamiBot = GamiBot.getInstance()

        GamiBot.getInstance().loginSDK(
            this,
            User(
                "riyadyhia@gmail.com", "Riyad Yahya",
                HashUtil.createHmacKey(
                    "riyadyhia@gmail.com|Riyad Yahya",
                    "e71f524cee7995766626bd40350d883d14ded66dc095a3b89fb71b89faa751ce"
                )
            )
        )

        GamiBot.getInstance().registerGamiphyOnAuthTrigger(object : OnAuthTrigger {
            override fun onAuthTrigger(isSignUp: Boolean) {
                if (!isSignUp) {
                    startActivity(Intent(applicationContext, LoginActivity::class.java))
                }
            }
        }
        )

        gamiBot.registerGamiphyOnTaskTrigger(object : OnTaskTrigger {
            override fun onTaskTrigger(actionName: String) {
                Log.d(MainActivity::class.java.simpleName, "here is action name $actionName")
            }
        })

//        gamiBot.registerGamiphyOnRedeemTrigger(object : OnRedeemTrigger {
//            override fun onRedeemTrigger(redeem: Redeem?) {
//                Log.d(
//                    MainActivity::class.java.simpleName,
//                    "here is package Id  ${redeem?.packageId} ----- ${redeem?.pointsToRedeem}"
//                )
//                GamiBot.getInstance()
//                    .markRedeemDone(redeem?.packageId!!, redeem.pointsToRedeem!!)
//            }
//        })

        button.setOnClickListener {
            // this is how to pass data object
            GamiBot.getInstance()
                .markTaskDoneSdk(
                    "purchaseCourseEvent",
                    "riyadyhia@gmail.com",
                    data = Client(2, "testClient")
                )
            // if there is no data
//            GamiBot.getInstance()
//                .markTaskDoneSdk("purchaseCourseEvent","riyadyhia@gmail.com",)
            GamiBot.getInstance().open(this, null, "en")
        }

        btn_new.setOnClickListener {
            startActivity(NewActivity.newIntent(this))
        }
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, MainActivity::class.java)
    }
}
