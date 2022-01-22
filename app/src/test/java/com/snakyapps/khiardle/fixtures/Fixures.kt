package com.snakyapps.khiardle.fixtures

import com.snakyapps.khiardle.backend.models.Word
import com.snakyapps.khiardle.backend.repository.WordRepository

class TestWordRepository : WordRepository {
    val words = mutableListOf(Word("TITLE"), Word("TESTY"))
    override fun find(word: Word): Boolean {
        return words.contains(word)
    }

    override fun random(): Word {
        return Word("TESTY")
    }

    fun addWord(word: Word) = words.add(word)
}

class AllExistRepository : WordRepository {
    override fun find(word: Word): Boolean {
        return true
    }

    override fun random(): Word {
        return Word("TESTY")
    }
}