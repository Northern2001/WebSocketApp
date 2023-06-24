package com.northern.websocketapp

import android.util.Log
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import java.util.concurrent.TimeUnit

class WebServicesProvider {
    private var _webSocket: WebSocket? = null

    private val socketOkHttpClient = OkHttpClient.Builder()
        .readTimeout(30, TimeUnit.SECONDS)
        .connectTimeout(30, TimeUnit.SECONDS)
        .hostnameVerifier { _, _ -> true }
        .build()


    @ExperimentalCoroutinesApi
    fun startSocket(webSocketListener: WebSocketAppListener) {
        socketOkHttpClient.newWebSocket(createRequest(), webSocketListener)
    }

    @ExperimentalCoroutinesApi
    fun stopSocket() {
        try {
            _webSocket?.close(Constants.NORMAL_CLOSURE_STATUS, null)
            _webSocket = null
        } catch (ex: Exception) {
            Log.e("socket stop", ex.message.toString())
        }
    }

    private fun createRequest(): Request {
        val websocketURL =
            "wss://${Constants.CLUSTER_ID}.piesocket.com/v3/1?api_key=${Constants.API_KEY}"
        return Request.Builder()
            .url(websocketURL)
            .build()
    }

}    
 