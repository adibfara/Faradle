package com.snakyapps.khiardle.fixtures

import com.snakyapps.khiardle.backend.models.Word
import com.snakyapps.khiardle.backend.repository.WordRepository

class TestWordRepository : WordRepository {
    val words = listOf(Word("TITLE"), Word("TESTY"))
    override fun find(word: Word): Boolean {
        return words.contains(word)
    }

}

class AllExistRepository : WordRepository {
    override fun find(word: Word): Boolean {
        return true
    }

}