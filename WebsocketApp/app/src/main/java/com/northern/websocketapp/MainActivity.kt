package com.northern.websocketapp

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.northern.websocketapp.databinding.ActivityMainBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.OkHttpClient
import okhttp3.WebSocket

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var webSocketListener: WebSocketAppListener
    private val okHttpClient = OkHttpClient()
    private var webSocket: WebSocket? = null
    private var webServicesProvider = WebServicesProvider()

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        webSocketListener = WebSocketAppListener(viewModel)

        viewModel.socketStatus.observe(this) {
            binding.status.text = getStatus(it)
            binding.status.setTextColor(if (it) Color.GREEN else Color.RED)
        }

        binding.btnDis.setOnClickListener {
            webServicesProvider.stopSocket()
        }

        binding.btnConnect.setOnClickListener {
            webServicesProvider.startSocket(webSocketListener)
        }

        binding.btnSend.setOnClickListener {
            webSocket?.send(binding.edtMess.text.toString())
            viewModel.addMessage(Pair(true, binding.edtMess.text.toString()))
        }

        setContentView(binding.root)
    }

    fun getStatus(isConnect: Boolean = false): String {
        return if (isConnect) "Connect" else "Disconnect"
    }

    override fun onDestroy() {
        super.onDestroy()
        okHttpClient.dispatcher.executorService.shutdown()
    }
}