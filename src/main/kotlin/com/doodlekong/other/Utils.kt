package com.doodlekong.other

import com.doodlekong.data.models.BaseModel
import com.doodlekong.data.models.ChatMessage
import com.doodlekong.data.models.DrawData
import com.doodlekong.other.Constants.TYPE_CHAT_MESSAGE
import com.doodlekong.other.Constants.TYPE_DRAW_DATA
import com.google.gson.JsonObject

fun JsonObject.getObjectType() = when (get("type").asString) {
    TYPE_CHAT_MESSAGE -> ChatMessage::class.java
    TYPE_DRAW_DATA -> DrawData::class.java
    else -> BaseModel::class.java
}
