package com.doodlekong.routes

import com.doodlekong.data.Player
import com.doodlekong.data.Room
import com.doodlekong.data.models.*
import com.doodlekong.gson
import com.doodlekong.other.getObjectType
import com.doodlekong.server
import com.doodlekong.session.DrawingSession
import com.google.gson.JsonParser
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consumeEach

fun Route.webSocketRoutes() {
    gameWebSocketRoute()
}

fun Route.gameWebSocketRoute() {
    route("/ws/draw") {
        standardWebSocket { socket, clientId, message, payload ->
            when (payload) {
                is JoinRoomHandshake -> {
                    val room = server.rooms[payload.roomName]
                    if (room == null) {
                        val gameError = GameError(GameError.ErrorType.ERROR_ROOM_NOT_FOUND)
                        socket.send(Frame.Text(gson.toJson(gameError)))
                        return@standardWebSocket
                    }
                    val player = Player(
                        username = payload.username,
                        socket = socket,
                        clientId = clientId
                    )
                    server.playerJoined(player)
                    if (!room.containsPlayer(player.username)) {
                        room.addPlayer(player.clientId, player.username, player.socket)
                    } else {
                        val playerInRoom = room.players.find { it.clientId == clientId }
                        playerInRoom?.socket = socket
                        playerInRoom?.startPinging()
                    }
                }
                is DrawData -> {
                    val room = server.rooms[payload.roomName] ?: return@standardWebSocket
                    if (room.phase == Room.Phase.GAME_RUNNING) {
                        room.broadcastToAllExcept(message, clientId)
                        room.addSerializedDrawInfo(message)
                    }
                    room.lastDrawData = payload
                }
                is DrawAction -> {
                    val room = server.getRoomWithClientId(clientId) ?: return@standardWebSocket
                    room.broadcastToAllExcept(message, clientId)
                    room.addSerializedDrawInfo(message)
                }
                is ChosenWord -> {
                    val room = server.rooms[payload.roomName] ?: return@standardWebSocket
                    room.setWordAndSwitchToGameRunning(payload.chosenWord)
                }
                is ChatMessage -> {
                    val room = server.rooms[payload.roomName] ?: return@standardWebSocket
                    if (!room.checkWordAndNotifyPlayers(payload)) {
                        room.broadcast(message)
                    }
                }
                is Ping -> {
                    server.players[clientId]?.receivedPong()
                }
                is DisconnectRequest -> {
                    server.playerLeft(clientId = clientId, immediatelyDisconnect = true)
                }
            }
        }
    }
}

fun Route.standardWebSocket(
    handleFrame: suspend (
        socket: DefaultWebSocketServerSession,
        clientId: String,
        message: String,
        payload: BaseModel
    ) -> Unit
) {
    webSocket {
        val session = call.sessions.get<DrawingSession>()
        if (session == null) {
            close(CloseReason(CloseReason.Codes.VIOLATED_POLICY, "No session."))
            return@webSocket
        }

        try {
            incoming.consumeEach { frame ->
                if (frame is Frame.Text) {
                    val message = frame.readText()
                    val jsonObject = JsonParser.parseString(message).asJsonObject
                    val type = jsonObject.getObjectType()
                    val payload = gson.fromJson(message, type)
                    handleFrame(this, session.clientId, message, payload)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            val playerWithClientId =
                server.getRoomWithClientId(session.clientId)?.players?.find { it.clientId == session.clientId }
            if (playerWithClientId != null) {
                server.playerLeft(session.clientId)
            }
        }
    }
}
