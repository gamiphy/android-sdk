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
                    "e71f524cee7995766626bd40350d883d14ded66dc095a3b89fb71b89faa751ce"
                )
            )
        )
        finish()
    }
}
