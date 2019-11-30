package com.gamiphy.library

import android.content.Context
import androidx.annotation.RestrictTo
import com.gamiphy.library.models.User

interface GamiBot {
    fun init(botId: String): GamiBot
    fun setBotId(botId: String): GamiBot
    fun setDebug(debug: Boolean)

    fun open(context: Context, user: User? = null)
    fun close()

    fun login(user: User)
    fun logout(context: Context)

    fun markTaskDone(eventName: String, quantity: Int? = null)
    fun markTaskDoneSdk(eventName: String, email: String, quantity: Int? = null, data: Any? = null)
    fun markRedeemDone(rewardId: String)

    @RestrictTo(RestrictTo.Scope.LIBRARY)
    fun registerGamiphyWebViewActions(gamiphyWebViewActions: GamiphyWebViewActions): GamiBotImpl

    @RestrictTo(RestrictTo.Scope.LIBRARY)
    fun unRegisterGamiphyWebViewActions(gamiphyWebViewActions: GamiphyWebViewActions): GamiBotImpl

    fun registerGamiphyOnAuthTrigger(onAuthTrigger: OnAuthTrigger): GamiBotImpl
    fun unRegisterGamiphyOnAuthTrigger(onAuthTrigger: OnAuthTrigger): GamiBotImpl
    fun registerGamiphyOnTaskTrigger(onTaskTrigger: OnTaskTrigger): GamiBotImpl
    fun unRegisterGamiphyOnTaskTrigger(onTaskTrigger: OnTaskTrigger): GamiBotImpl
    fun registerGamiphyOnRedeemTrigger(onRedeemTrigger: OnRedeemTrigger): GamiBotImpl
    fun unRegisterGamiphyOnRedeemTrigger(onRedeemTrigger: OnRedeemTrigger): GamiBotImpl
    fun notifyAuthTrigger(signUp: Boolean)
    fun notifyTaskTrigger(actionName: String)
    fun notifyRedeemTrigger(rewardId: String)
    fun loginSDK(context: Context, user: User)

    companion object {
        private var instance: GamiBotImpl? = null

        fun getInstance(): GamiBot {
            if (instance == null) {
                instance = GamiBotImpl()
            }
            return instance!!
        }
    }
}