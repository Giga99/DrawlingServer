package com.doodlekong.data.models

import com.doodlekong.other.Constants.TYPE_NEW_WORDS

data class NewWords(
    val newWords: List<String>
) : BaseModel(type = TYPE_NEW_WORDS)
