package com.gamiphy.library

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.gamiphy.library.network.RetrofitClient
import com.gamiphy.library.network.models.responses.bot.BotOptions
import com.gamiphy.library.utils.GamiphyData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class GamiphyBotButton @kotlin.jvm.JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    init {
        val imageViewParam = LayoutParams(100, 100)
        val simpleImageView = ImageView(context)
        simpleImageView.layoutParams = imageViewParam // set defined layout params to ImageView
        imageViewParam.addRule(CENTER_IN_PARENT) // align ImageView in the center
        addView(simpleImageView) // add ImageView in RelativeLayout

        val gamiphyData = GamiphyData.getInstance()
        val call: Call<BotOptions> = RetrofitClient.gamiphyApiServices
            .getBotOptions(gamiphyData.botId)
        call.enqueue(object : Callback<BotOptions> {

            override fun onResponse(call: Call<BotOptions>, response: Response<BotOptions>) {
                if (response.isSuccessful) {
                    val bot = response.body()?.bot

                    Glide
                        .with(context)
                        .load(bot?.style?.launcher?.icon)
                        .apply(RequestOptions().centerCrop())
                        .apply(RequestOptions().centerInside())
//                        .apply(RequestOptions().placeholder(R.drawable.ic_android))
//                        .apply(RequestOptions().error(R.drawable.ic_android))
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(simpleImageView)

                    background = when (bot?.style?.launcher?.shape) {
                        "Oval" -> {
                            ContextCompat.getDrawable(context, R.drawable.circle_bg)
                        }
                        "Rectangle" -> {
                            ContextCompat.getDrawable(context, R.drawable.rounded_square_bg)
                        }
                        "Rhombus" -> {
                            ContextCompat.getDrawable(context, R.drawable.rhoumbus_bg)
                        }
                        "Rounded" -> {
                            layoutParams = LayoutParams((width * 1.5).toInt(), height)
                            ContextCompat.getDrawable(context, R.drawable.rounded_square_bg)
                        }
                        else -> {
                            ContextCompat.getDrawable(context, R.drawable.circle_bg)
                        }
                    }
                    DrawableCompat.setTint(background, Color.parseColor(bot?.style?.brandColor ?: "#FFFFFF"))
                }
            }

            override fun onFailure(call: Call<BotOptions>, t: Throwable) {
                Log.e(GamiphyBotButton::class.java.simpleName, t.message, t)
                visibility = View.INVISIBLE
            }
        })

        setOnClickListener { GamiBot.getInstance().open(context) }
    }
}