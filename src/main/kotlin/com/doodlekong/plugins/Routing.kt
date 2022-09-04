package com.doodlekong.plugins

import com.doodlekong.data.models.BasicApiResponse
import com.doodlekong.routes.roomRoutes
import com.doodlekong.routes.webSocketRoutes
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    install(Routing) {
        homeRoute()
        roomRoutes()
        webSocketRoutes()
    }
}

fun Route.homeRoute() {
    route("/") {
        get {
            call.respond(HttpStatusCode.OK, BasicApiResponse(successful = true, message = "Server is running!"))
        }
    }
}
