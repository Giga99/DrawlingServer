package com.doodlekong.data.models

import com.doodlekong.other.Constants.TYPE_JOIN_ROOM_HANDSHAKE

data class JoinRoomHandshake(
    val username: String,
    val roomName: String,
    val clientId: String
) : BaseModel(type = TYPE_JOIN_ROOM_HANDSHAKE)
