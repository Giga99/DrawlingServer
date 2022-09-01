package com.doodlekong.data.models

import com.doodlekong.other.Constants.TYPE_PLAYERS_LIST

data class PlayersList(
    val players: List<PlayerData>
) : BaseModel(type = TYPE_PLAYERS_LIST)
