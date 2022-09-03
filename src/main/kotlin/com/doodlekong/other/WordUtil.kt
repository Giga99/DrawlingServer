package com.doodlekong.other

import java.io.File

fun readWordList(fileName: String): List<String> {
    val inputStream = File(fileName).inputStream()
    val words = mutableListOf<String>()
    inputStream.bufferedReader().forEachLine { words.add(it) }

    return words
}

fun List<String>.getRandomWords(amount: Int): List<String> {
    var curAmount = 0
    val result = mutableListOf<String>()
    while (curAmount < amount) {
        val word = this.random()
        if (!result.contains(word)) {
            result.add(word)
            curAmount++
        }
    }
    return result
}

fun String.transformToUnderscores() = toCharArray().map { if (it != ' ') '_' else ' ' }.joinToString(" ")
