package com.doodlekong.other

import com.doodlekong.data.models.ChatMessage

fun ChatMessage.matchesWord(word: String): Boolean = message.lowercase().trim() == word.lowercase().trim()
