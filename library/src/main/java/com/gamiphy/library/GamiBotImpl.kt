package com.gamiphy.library

import android.content.Context
import android.util.Log
import androidx.annotation.RestrictTo
import androidx.core.content.edit
import com.gamiphy.library.models.User
import com.gamiphy.library.network.RetrofitClient
import com.gamiphy.library.network.models.LoginResponse
import com.gamiphy.library.network.models.TrackEventRequest
import com.gamiphy.library.network.models.TrackEventResponse
import com.gamiphy.library.ui.GamiphyWebViewActivity
import com.gamiphy.library.utils.GamiphyData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@RestrictTo(RestrictTo.Scope.LIBRARY)
class GamiBotImpl : GamiBot {
    private val gamiphyData = GamiphyData.getInstance()
    private val gamiphyWebViewActionsList = mutableListOf<GamiphyWebViewActions>()
    private val gamiphyOnAuthTriggerListeners = mutableListOf<OnAuthTrigger>()
    private val gamiphyOnTaskTriggerListeners = mutableListOf<OnTaskTrigger>()
    private val gamiphyOnRedeemTriggerListeners = mutableListOf<OnRedeemTrigger>()

    override fun init(botId: String): GamiBot {
        gamiphyData.botId = botId
        return this
    }

    override fun setDebug(debug: Boolean) {
        gamiphyData.debug = debug
    }

    override fun open(context: Context, user: User?) {
        gamiphyData.token = context.getSharedPreferences(
            TOKEN_PREF,
            Context.MODE_PRIVATE
        ).getString(TOKEN_PREF_ID, null)
        context.startActivity(GamiphyWebViewActivity.newIntent(context))
    }

    override fun setBotId(botId: String): GamiBotImpl {
        gamiphyData.botId = botId
        return this
    }

    override fun markRedeemDone(rewardId: String) {
        gamiphyWebViewActionsList.forEach {
            it.markRedeemDone(rewardId)
        }
    }

    override fun markTaskDone(eventName: String, quantity: Int?) {
        gamiphyWebViewActionsList.forEach {
            it.markTaskDone(eventName)
        }
    }

    override fun markTaskDoneSdk(eventName: String, email: String, quantity: Int?, data: Any?) {
        val call: Call<TrackEventResponse> = RetrofitClient.gamiphyApiServices
            .sendTrack(
                GamiphyData.getInstance().botId,
                TrackEventRequest(eventName, email, data)
            )
        call.enqueue(object : Callback<TrackEventResponse> {
            override fun onFailure(call: Call<TrackEventResponse>, t: Throwable) {
                Log.e(GamiBotImpl::class.java.name, t.message, t)
            }

            override fun onResponse(
                call: Call<TrackEventResponse>,
                response: Response<TrackEventResponse>
            ) {
                Log.d(GamiBotImpl::class.java.name, "success")
            }

        })
    }

    override fun loginSDK(context: Context, user: User) {
        val call: Call<LoginResponse> = RetrofitClient.gamiphyApiServices
            .loginSDK(
                GamiphyData.getInstance().botId,
                user
            )
        call.enqueue(object : Callback<LoginResponse> {
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e(GamiBotImpl::class.java.name, t.message, t)
            }

            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                val token = response.body()?.token
                context.getSharedPreferences(
                    TOKEN_PREF,
                    Context.MODE_PRIVATE
                ).edit {
                    putString(TOKEN_PREF_ID, token)
                }
                Log.d(GamiBotImpl::class.java.name, "success")
            }

        })
    }


    override fun login(user: User) {
        gamiphyData.user = user
        gamiphyWebViewActionsList.forEach {
            it.login()
        }
    }

    override fun logout(context: Context) {
        context.getSharedPreferences(
            TOKEN_PREF,
            Context.MODE_PRIVATE
        ).edit { clear() }
        gamiphyWebViewActionsList.forEach {
            it.logout()
        }
    }

    override fun close() {
        gamiphyWebViewActionsList.forEach {
            it.close()
        }
    }

    override fun registerGamiphyWebViewActions(gamiphyWebViewActions: GamiphyWebViewActions): GamiBotImpl {
        gamiphyWebViewActionsList.add(gamiphyWebViewActions)
        return this
    }

    override fun unRegisterGamiphyWebViewActions(gamiphyWebViewActions: GamiphyWebViewActions): GamiBotImpl {
        gamiphyWebViewActionsList.remove(gamiphyWebViewActions)
        return this
    }

    override fun registerGamiphyOnAuthTrigger(onAuthTrigger: OnAuthTrigger): GamiBotImpl {
        gamiphyOnAuthTriggerListeners.add(onAuthTrigger)
        return this
    }

    override fun unRegisterGamiphyOnAuthTrigger(onAuthTrigger: OnAuthTrigger): GamiBotImpl {
        gamiphyOnAuthTriggerListeners.remove(onAuthTrigger)
        return this
    }

    override fun registerGamiphyOnTaskTrigger(onTaskTrigger: OnTaskTrigger): GamiBotImpl {
        gamiphyOnTaskTriggerListeners.add(onTaskTrigger)
        return this
    }

    override fun unRegisterGamiphyOnTaskTrigger(onTaskTrigger: OnTaskTrigger): GamiBotImpl {
        gamiphyOnTaskTriggerListeners.remove(onTaskTrigger)
        return this
    }

    override fun registerGamiphyOnRedeemTrigger(onRedeemTrigger: OnRedeemTrigger): GamiBotImpl {
        gamiphyOnRedeemTriggerListeners.add(onRedeemTrigger)
        return this
    }

    override fun unRegisterGamiphyOnRedeemTrigger(onRedeemTrigger: OnRedeemTrigger): GamiBotImpl {
        gamiphyOnRedeemTriggerListeners.remove(onRedeemTrigger)
        return this
    }

    override fun notifyAuthTrigger(signUp: Boolean) {
        gamiphyOnAuthTriggerListeners.forEach {
            it.onAuthTrigger(signUp)
        }
    }

    override fun notifyTaskTrigger(actionName: String) {
        gamiphyOnTaskTriggerListeners.forEach {
            it.onTaskTrigger(actionName)
        }
    }

    override fun notifyRedeemTrigger(rewardId: String) {
        gamiphyOnRedeemTriggerListeners.forEach {
            it.onRedeemTrigger(rewardId)
        }
    }

    companion object {
        private const val TOKEN_PREF = "TOKEN_PREF"
        private const val TOKEN_PREF_ID = "TOKEN_PREF_ID"
    }
}