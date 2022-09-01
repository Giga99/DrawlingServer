package com.doodlekong.other

import com.doodlekong.data.models.*
import com.doodlekong.other.Constants.TYPE_ANNOUNCEMENT
import com.doodlekong.other.Constants.TYPE_CHAT_MESSAGE
import com.doodlekong.other.Constants.TYPE_CHOSEN_WORD
import com.doodlekong.other.Constants.TYPE_DISCONNECT_REQUEST
import com.doodlekong.other.Constants.TYPE_DRAW_ACTION
import com.doodlekong.other.Constants.TYPE_DRAW_DATA
import com.doodlekong.other.Constants.TYPE_GAME_STATE
import com.doodlekong.other.Constants.TYPE_JOIN_ROOM_HANDSHAKE
import com.doodlekong.other.Constants.TYPE_PHASE_CHANGE
import com.doodlekong.other.Constants.TYPE_PING
import com.google.gson.JsonObject

fun JsonObject.getObjectType() = when (get("type").asString) {
    TYPE_CHAT_MESSAGE -> ChatMessage::class.java
    TYPE_DRAW_DATA -> DrawData::class.java
    TYPE_ANNOUNCEMENT -> Announcement::class.java
    TYPE_JOIN_ROOM_HANDSHAKE -> JoinRoomHandshake::class.java
    TYPE_PHASE_CHANGE -> PhaseChange::class.java
    TYPE_CHOSEN_WORD -> ChosenWord::class.java
    TYPE_GAME_STATE -> GameState::class.java
    TYPE_PING -> Ping::class.java
    TYPE_DISCONNECT_REQUEST -> DisconnectRequest::class.java
    TYPE_DRAW_ACTION -> DrawAction::class.java
    else -> BaseModel::class.java
}
