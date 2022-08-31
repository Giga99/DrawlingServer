package com.doodlekong.data.models

import com.doodlekong.other.Constants.TYPE_CHOSEN_WORD

data class ChosenWord(
    val chosenWord: String,
    val roomName: String
) : BaseModel(type = TYPE_CHOSEN_WORD)
