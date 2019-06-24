package com.gamiphy.library.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.*
import android.widget.ProgressBar
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.gamiphy.library.*
import com.gamiphy.library.network.models.responses.Action
import com.gamiphy.library.network.models.responses.ActionData
import com.gamiphy.library.utils.GamiphyData
import com.gamiphy.library.utils.JavaScriptScripts
import com.gamiphy.library.utils.JavaScriptScripts.JAVASCRIPT_OBJ
import com.google.gson.Gson
import com.gamiphy.library.network.models.responses.redeem.Redeem
import com.gamiphy.library.utils.GamiphyConstants
import com.google.gson.reflect.TypeToken


class GamiphyWebViewActivity : AppCompatActivity(), GamiphyWebViewActions {
    private lateinit var webView: WebView
    private lateinit var progressBar: ProgressBar
    private val gamiBot = GamiBot.getInstance()
    private val gamiphyData = GamiphyData.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gamiphy_web_view)
        gamiBot.registerGamiphyWebViewActions(this)
        initViews()
    }

    override fun onDestroy() {
        super.onDestroy()
        gamiBot.unRegisterGamiphyWebViewActions(this)
        webView.removeJavascriptInterface(JAVASCRIPT_OBJ)
    }

    override fun login() {
        postTokenMessage()
    }

    override fun logout() {
        gamiphyData.user.name = ""
        gamiphyData.user.email = ""
        postTokenMessage()
    }

    override fun close() {
        finish()
    }

    override fun refresh() {
        initWebView("https://" + gamiphyData.botId + ".test.bot.gamiphy.co/")
    }

    override fun markTaskDone(eventName: String) {
        executeJavaScript(JavaScriptScripts.trackEvent(eventName))
    }

    override fun markRedeemDone(rewardId: String) {
        executeJavaScript(JavaScriptScripts.redeemReward(rewardId))
    }

    private fun initViews() {
        webView = findViewById(R.id.webView)
        progressBar = findViewById(R.id.progressBar)
        initWebView("https://" + gamiphyData.botId + GamiphyConstants.BOT_API)
    }

    @SuppressLint("SetJavaScriptEnabled", "AddJavascriptInterface")
    private fun initWebView(url: String) {
        val webSettings = webView.settings
        webView.webChromeClient = WebChromeClient()
        with(webSettings) {
            javaScriptEnabled = true
            domStorageEnabled = true
            javaScriptCanOpenWindowsAutomatically = true
            builtInZoomControls = true
            blockNetworkImage = false
            loadsImagesAutomatically = true
            supportMultipleWindows()
        }
        webView.loadUrl(url)
        webView.addJavascriptInterface(
            JavaScriptInterface(), JAVASCRIPT_OBJ
        )

        webView.webViewClient = object : WebViewClient() {

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                showLoading()
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                hideLoading()
                postTokenMessage()
                executeJavaScript(JavaScriptScripts.addGamiphyEvent())
            }

            @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                val requestUrl = request?.url.toString()
                view?.loadUrl(requestUrl)
                return false
            }

            override fun onReceivedSslError(
                view: WebView?,
                handler: SslErrorHandler?,
                error: SslError?
            ) {
                hideLoading()
            }


            override fun onReceivedHttpError(
                view: WebView?,
                request: WebResourceRequest?,
                errorResponse: WebResourceResponse?
            ) {
                hideLoading()
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                hideLoading()
            }
        }
    }

    private fun postTokenMessage() {
        if (gamiphyData.user.email.isEmpty()) {
            executeJavaScript(JavaScriptScripts.initBot())
        } else {
            executeJavaScript(JavaScriptScripts.initBot(gamiphyData.user))
        }
    }

    private fun executeJavaScript(script: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.post {  webView.evaluateJavascript(script, null)}
        } else {
            webView.post {webView.loadUrl(script, null)  }

        }
    }

    private fun hideLoading() {
        progressBar.visibility = View.GONE
    }

    private fun showLoading() {
        progressBar.visibility = View.VISIBLE
    }

    private inner class JavaScriptInterface {
        @JavascriptInterface
        fun pathFromWeb(isSignUp: Boolean) {
            gamiBot.notifyAuthTrigger(isSignUp)
        }

        @JavascriptInterface
        fun eventFromWeb(event: String) {
            Log.d(GamiphyWebViewActivity::class.java.simpleName, "=====<>>>>$event")
            val actionType = object : TypeToken<Action<ActionData>>() {}.type
            val action = Gson().fromJson<Action<ActionData>>(event, actionType)
            if (action.type == "actionTrigger") {
                gamiBot.notifyTaskTrigger(action.data.actionName)
            } else if (action.type == "redeemTrigger") {
                val redeemType = object : TypeToken<Action<Redeem>>() {}.type
                val redeem = Gson().fromJson<Action<Redeem>>(event, redeemType)
                Log.d(GamiphyWebViewActivity::class.java.simpleName, redeem.data.reward._id)
                Log.d(GamiphyWebViewActivity::class.java.simpleName, redeem.data.reward.options.value)
                gamiBot.notifyRedeemTrigger(redeem.data.reward._id)
            }
        }
    }

    companion object {
        @JvmStatic
        fun newIntent(context: Context) = Intent(context, GamiphyWebViewActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
    }
}