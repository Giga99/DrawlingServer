package com.doodlekong

import com.doodlekong.plugins.*
import com.google.gson.Gson
import io.ktor.server.engine.*
import io.ktor.server.netty.*

val server = DrawingServer()
val gson = Gson()

fun main() {
    embeddedServer(
        Netty,
        port = System.getenv("PORT")?.toInt() ?: 8000,
        host = "0.0.0.0"
    ) {
        configureSerialization()
        configureSockets()
        configureMonitoring()
        configureRouting()
        configureSessions()
    }.start(wait = true)
}
