package com.doodlekong.data

import com.doodlekong.data.models.Ping
import com.doodlekong.gson
import com.doodlekong.other.Constants.PING_FREQUENCY
import com.doodlekong.server
import io.ktor.websocket.*
import kotlinx.coroutines.*

@OptIn(DelicateCoroutinesApi::class)
data class Player(
    val username: String,
    var socket: WebSocketSession,
    val clientId: String,
    var isDrawing: Boolean = false,
    var score: Int = 0,
    var rank: Int = 0
) {
    private var pingJob: Job? = null

    private var pingTime = 0L
    private var pongtime = 0L

    var isOnline = true

    fun startPinging() {
        pingJob?.cancel()
        pingJob = GlobalScope.launch {
            while (true) {
                sendPing()
                delay(PING_FREQUENCY)
            }
        }
    }

    private suspend fun sendPing() {
        pingTime = System.currentTimeMillis()
        socket.send(Frame.Text(gson.toJson(Ping())))
        delay(PING_FREQUENCY)
        if (pingTime - pongtime > PING_FREQUENCY) {
            isOnline = false
            server.playerLeft(clientId)
            pingJob?.cancel()
        }
    }

    fun receivedPong() {
        pongtime = System.currentTimeMillis()
        isOnline = true
    }

    fun disconnect() {
        pingJob?.cancel()
    }
}
