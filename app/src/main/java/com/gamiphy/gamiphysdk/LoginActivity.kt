package com.gamiphy.gamiphysdk

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.gamiphy.library.GamiBot
import com.gamiphy.library.models.User
import com.gamiphy.library.utils.GamiphyData
import com.gamiphy.library.utils.HashUtil

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        GamiBot.getInstance().login(
            User(
                "abdallah@gamiphy.co", "abdallah AbuSalah",
                HashUtil.createHmacKey(
                    "abdallah@gamiphy.co|abdallah AbuSalah",
                    "94c711455c8fabb3c3ffacace7711eda10be9d1147afa140872af60b026ebfca"
                )
            )
        )
        GamiBot.getInstance().open(this)
        finish()
    }
}
