package com.gamiphy.library.utils

import com.gamiphy.library.models.User
import com.google.gson.Gson

object JavaScriptScripts {

    fun initBot(user: User? = null, language: String? = "en") = user?.let {
        val userJson = Gson().toJson(user).toString()
        "javascript: window.postMessage({origin: 'Gamiphy', type: 'initialize', data: {user: $userJson, language: '$language', parentWindow:{type: 'Android'}}},'*')"
    } ?: let { "javascript: window.postMessage({origin: 'Gamiphy', type: 'initialize',data:{logout: true, language: '$language', parentWindow:{type: 'Android'}}},'*')" }

    fun initBot(token: String, language: String? = "en") =
        "javascript: window.postMessage({origin: 'Gamiphy', type: 'initialize', data: {user: '$token', language: '$language', parentWindow:{type: 'Android'}}},'*')"

    fun addGamiphyEvent() =
        "javascript: " +
                "window.parent.addEventListener('message', function (event) {" +
                "        if (!event.data || (event.data && event.data.origin !== 'Gamiphy')) {" +
                "            return;" +
                "        }" +
                "        if (event.data.type === 'authTrigger') {\n" +
                JAVASCRIPT_OBJ + ".pathFromWeb(JSON.stringify(event.data.data.isSignUp));     " +
                "        }else {" +
                JAVASCRIPT_OBJ + ".eventFromWeb(JSON.stringify(event.data)); }    " +
                "    });"

    fun trackEvent(eventName: String) =
        "javascript: window.postMessage({origin: 'Gamiphy', type: 'trackEvent', data: {eventName: '$eventName'}},'*')"

    fun redeemReward(rewardId: String) =
        "javascript: window.postMessage({origin: 'Gamiphy', type: 'redeemReward', data: {rewardId: '$rewardId'}},'*')"

    const val JAVASCRIPT_OBJ = "javascript_obj"
}