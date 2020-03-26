package com.gamiphy.gamiphysdk

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.gamiphy.library.GamiBot
import com.gamiphy.library.OnAuthTrigger
import com.gamiphy.library.OnTaskTrigger
import com.gamiphy.library.models.User
import com.gamiphy.library.utils.HashUtil

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.bot)
        val gamiBot = GamiBot.getInstance()

        GamiBot.getInstance().loginSDK(
            this, User(
                "abdallah@gamiphy.co", "abdallah AbuSalah",
                HashUtil.createHmacKey(
                    "abdallah@gamiphy.co|abdallah AbuSalah",
                    "94c711455c8fabb3c3ffacace7711eda10be9d1147afa140872af60b026ebfca"
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

        button.setOnClickListener {
            GamiBot.getInstance().open(this, null, "en")
        }
    }
}
