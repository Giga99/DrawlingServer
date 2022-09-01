package com.doodlekong.data.models

import com.doodlekong.other.Constants.TYPE_GAME_STATE

data class GameState(
    val drawingPlayer: String,
    val word: String
) : BaseModel(type = TYPE_GAME_STATE)
