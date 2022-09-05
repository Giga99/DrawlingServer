package com.doodlekong.data

enum class WordList {
    PROGRAMMERS_WORDLIST,
    SERBIAN_WORDLIST,
    DEFAULT_WORDLIST;

    fun toFileName() =
        when (this) {
            PROGRAMMERS_WORDLIST -> "programmers_wordlist.txt"
            SERBIAN_WORDLIST -> "serbian_wordlist.txt"
            else -> "default_wordlist.txt"
        }

    companion object {
        const val WORDLIST_LOCATION = "src/main/resources/"
    }
}
