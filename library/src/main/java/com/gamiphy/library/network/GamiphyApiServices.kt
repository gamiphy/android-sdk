package com.gamiphy.library.network

import com.gamiphy.library.network.models.TrackEventRequest
import com.gamiphy.library.network.models.TrackEventResponse
import com.gamiphy.library.network.models.responses.bot.BotOptions
import com.gamiphy.library.utils.GamiphyConstants
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Api services for the Gamiphy bot.
 */
interface GamiphyApiServices {
    @GET("bots/" + GamiphyConstants.API_V1 + "{botId}/options")
    fun getBotOptions(@Path("botId") botId: String): Call<BotOptions>

    @POST("bot-event/" + GamiphyConstants.API_V1 + "/bot/{botId}/track")
    fun sendTrack(@Path("botId") botId: String, @Body trackEventRequest: TrackEventRequest): Call<TrackEventResponse>
}