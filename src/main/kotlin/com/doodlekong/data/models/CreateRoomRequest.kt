package com.doodlekong.data.models

import com.doodlekong.data.WordList

data class CreateRoomRequest(
    val name: String,
    val maxPlayers: Int,
    val wordList: WordList
)
