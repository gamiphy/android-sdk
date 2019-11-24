package com.gamiphy.gamiphysdk

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.gamiphy.library.GamiBot
import com.gamiphy.library.OnAuthTrigger
import com.gamiphy.library.OnRedeemTrigger
import com.gamiphy.library.OnTaskTrigger

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.bot)
        val gamiBot = GamiBot.getInstance()

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

        gamiBot.registerGamiphyOnRedeemTrigger(object : OnRedeemTrigger {
            override fun onRedeemTrigger(rewardId: String) {
                Log.d(MainActivity::class.java.simpleName, "here is reward Id  $rewardId")
                GamiBot.getInstance().markRedeemDone(rewardId)
            }
        })

        button.setOnClickListener {
            // this is how to pass data object
            GamiBot.getInstance()
                .markTaskDoneSdk("purchaseCourseEvent", data = Client(2, "testClient"))
            // if there is no data
//            GamiBot.getInstance()
//                .markTaskDoneSdk("purchaseCourseEvent")
//            GamiBot.getInstance().open(this)


        }
    }
}
