package com.gamiphy.gamiphysdk

import android.os.Bundle
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
                "riyadyhia@gmail.com", "Riyad Yahya",
                HashUtil.createHmacKey(
                    "riyadyhia@gmail.com|Riyad Yahya",
                    "c7e692e1d371d090a3ffcdda4e8ee29be9baef18a6f39049f393ca742c8e46ad"
                )
            )
        )
        GamiBot.getInstance().open(this)
        finish()
    }
}
