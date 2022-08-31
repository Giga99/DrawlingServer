package com.doodlekong.plugins

import com.doodlekong.routes.roomRoutes
import com.doodlekong.routes.webSocketRoutes
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    install(Routing) {
        roomRoutes()
        webSocketRoutes()
    }
}
