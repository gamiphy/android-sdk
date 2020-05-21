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
import com.gamiphy.library.GamiBot
import com.gamiphy.library.GamiphyWebViewActions
import com.gamiphy.library.R
import com.gamiphy.library.network.models.responses.Action
import com.gamiphy.library.network.models.responses.ActionData
import com.gamiphy.library.network.models.responses.ShareData
import com.gamiphy.library.network.models.responses.redeem.Redeem
import com.gamiphy.library.utils.GamiphyConstants
import com.gamiphy.library.utils.GamiphyData
import com.gamiphy.library.utils.JavaScriptScripts
import com.gamiphy.library.utils.JavaScriptScripts.JAVASCRIPT_OBJ
import com.google.gson.Gson
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
        gamiphyData.token = null
        postTokenMessage()
    }

    override fun onBackPressed() {
        moveTaskToBack(true);
    }

    override fun close() {
        finish()
    }

    override fun refresh() {
        initWebView("https://" + gamiphyData.botId + GamiphyConstants.BOT_API)
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

    /**
     * init web view with token if exist or user.email
     * else open unSigned web view
     */
    private fun postTokenMessage() {
        val token = gamiphyData.token
        val language = gamiphyData.language
        if (!token.isNullOrEmpty()) {
            executeJavaScript(JavaScriptScripts.initBot(token, language))
        } else if (gamiphyData.user.email.isNotEmpty()) {
            executeJavaScript(JavaScriptScripts.initBot(gamiphyData.user, language))
        } else {
            executeJavaScript(JavaScriptScripts.initBot())
        }
    }

    private fun executeJavaScript(script: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.post { webView.evaluateJavascript(script, null) }
        } else {
            webView.post { webView.loadUrl(script, null) }
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

            when {
                action.type == ACTION_TRIGGER -> gamiBot.notifyTaskTrigger(action.data.actionName)

                action.type == REDEEM_TRIGGER -> {
                    val redeemType = object : TypeToken<Action<Redeem>>() {}.type
                    val redeem = Gson().fromJson<Action<Redeem>>(event, redeemType)
                    gamiBot.notifyRedeemTrigger(redeem.data)
                }

                action.type == SHARE -> {
                    val shareDataType = object : TypeToken<Action<ShareData>>() {}.type
                    val shareData = Gson().fromJson<Action<ShareData>>(event, shareDataType)
                    share(shareData.data.text, shareData.data.link)
                }
            }
        }
    }

    private fun share(text: String?, link: String?) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "$text \n $link")
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    companion object {
        private const val ACTION_TRIGGER = "actionTrigger"
        private const val REDEEM_TRIGGER = "redeemTrigger"
        private const val SHARE = "share"

        @JvmStatic
        fun newIntent(context: Context) =
            Intent(context, GamiphyWebViewActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
    }
}