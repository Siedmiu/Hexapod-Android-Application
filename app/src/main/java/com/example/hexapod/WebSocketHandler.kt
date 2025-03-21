package com.example.hexapod

import android.os.Handler
import android.os.Looper
import androidx.compose.runtime.mutableStateOf
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import java.util.Timer
import java.util.TimerTask
import java.util.concurrent.TimeUnit

object WebSocketHandler {
    private val client = OkHttpClient.Builder()
        .pingInterval(5, TimeUnit.SECONDS)
        .build()
    private const val IP = "192.169.0.201"
    private const val PORT = 8765
    private const val URL = "ws://$IP:$PORT/"

    private var webSocket: WebSocket? = null
    private var connectionActive = mutableStateOf(false)
    private val mainHandler = Handler(Looper.getMainLooper())

    private var autoReconnect = true
    private var reconnectTimer: Timer? = null
    private const val RECONNECT_INTERVAL = 5000L

    fun connect() {
        val request = Request.Builder().url(URL).build()
        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                mainHandler.post {
                    connectionActive.value = true
                }
                cancelReconnectTimer()
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                mainHandler.post {
                    connectionActive.value = false
                }
                scheduleReconnect()
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                mainHandler.post {
                    connectionActive.value = false
                }
                webSocket.close(code, reason)
                scheduleReconnect()
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                mainHandler.post {
                    connectionActive.value = false
                }
                scheduleReconnect()
            }
        })
    }

    fun disconnect() {
        autoReconnect = false
        webSocket?.close(1000, "Closing connection")
        webSocket = null
        connectionActive.value = false
        cancelReconnectTimer()
    }

    private fun scheduleReconnect() {
        if (!autoReconnect || reconnectTimer != null) return

        reconnectTimer = Timer().apply {
            schedule(object : TimerTask() {
                override fun run() {
                    mainHandler.post {
                        if (!connectionActive.value && autoReconnect) {
                            connect()
                        } else {
                            cancelReconnectTimer()
                        }
                    }
                }
            }, RECONNECT_INTERVAL, RECONNECT_INTERVAL)
        }
    }

    private fun cancelReconnectTimer() {
        reconnectTimer?.cancel()
        reconnectTimer?.purge()
        reconnectTimer = null
    }

    fun sendLedColor(ledColor: Int) {
        val message = "{\"led_color\": $ledColor}"
        webSocket?.send(message)
    }

    fun isConnected(): Boolean {
        return connectionActive.value
    }
}
