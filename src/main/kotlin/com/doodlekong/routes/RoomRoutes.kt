package com.doodlekong.routes

import com.doodlekong.data.Room
import com.doodlekong.data.WordList
import com.doodlekong.data.models.BasicApiResponse
import com.doodlekong.data.models.CreateRoomRequest
import com.doodlekong.data.models.RoomResponse
import com.doodlekong.other.Constants.MAX_ROOM_SIZE
import com.doodlekong.other.readWordList
import com.doodlekong.server
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.roomRoutes() {
    createRoomRoute()
    getRoomsRoute()
    joinRoomRoute()
    getAvailableWordListsRoute()
}

fun Route.createRoomRoute() {
    route("/api/createRoom") {
        post {
            val roomRequest = call.receiveOrNull<CreateRoomRequest>()
            if (roomRequest == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            if (server.rooms[roomRequest.name] != null) {
                call.respond(
                    status = HttpStatusCode.OK,
                    message = BasicApiResponse(false, "Room already exists.")
                )
                return@post
            }
            if (roomRequest.maxPlayers < 2) {
                call.respond(
                    status = HttpStatusCode.OK,
                    message = BasicApiResponse(false, "The minimum room size is 2.")
                )
                return@post
            }
            if (roomRequest.maxPlayers > MAX_ROOM_SIZE) {
                call.respond(
                    status = HttpStatusCode.OK,
                    message = BasicApiResponse(false, "The maximum room size is $MAX_ROOM_SIZE.")
                )
                return@post
            }

            val room = Room(
                name = roomRequest.name,
                maxPlayers = roomRequest.maxPlayers,
                words = readWordList("${WordList.WORDLIST_LOCATION}${roomRequest.wordList.toFileName()}")
            )
            server.rooms[roomRequest.name] = room
            println("Room created: ${roomRequest.name}")

            call.respond(HttpStatusCode.OK, BasicApiResponse(true))
        }
    }
}

fun Route.getRoomsRoute() {
    route("/api/getRooms") {
        get {
            val searchQuery = call.parameters["searchQuery"]
            if (searchQuery == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }

            val roomsResult = server.rooms.filterKeys {
                it.contains(searchQuery, ignoreCase = true)
            }
            val roomResponses = roomsResult.values.map {
                RoomResponse(it.name, it.maxPlayers, it.players.size)
            }.sortedBy { it.name }

            call.respond(HttpStatusCode.OK, roomResponses)
        }
    }
}

fun Route.joinRoomRoute() {
    route("/api/joinRoom") {
        get {
            val username = call.parameters["username"]
            val roomName = call.parameters["roomName"]
            if (username == null || roomName == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }

            val room = server.rooms[roomName]
            when {
                room == null -> {
                    call.respond(
                        status = HttpStatusCode.OK,
                        message = BasicApiResponse(false, "Room not found.")
                    )
                }
                room.containsPlayer(username) -> {
                    call.respond(
                        status = HttpStatusCode.OK,
                        message = BasicApiResponse(false, "A player with this username already joined.")
                    )
                }
                room.players.size >= room.maxPlayers -> {
                    call.respond(
                        status = HttpStatusCode.OK,
                        message = BasicApiResponse(false, "This room is already full.")
                    )
                }
                else -> {
                    call.respond(HttpStatusCode.OK, BasicApiResponse(true))
                }
            }
        }
    }
}

fun Route.getAvailableWordListsRoute() {
    route("/api/getAvailableWordLists") {
        get {
            val wordListResponse = WordList.values()
            call.respond(HttpStatusCode.OK, wordListResponse)
        }
    }
}
