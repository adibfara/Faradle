package com.snakyapps.khiardle.fixtures

import com.snakyapps.khiardle.backend.models.Level
import com.snakyapps.khiardle.backend.models.Word
import com.snakyapps.khiardle.backend.repository.LevelRepository
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
    override fun getWordForLevel(currentLevelNumber: Long): Word {
        return words[currentLevelNumber.toInt() - 1]
    }
}

class AllExistRepository : WordRepository {
    override fun find(word: Word): Boolean {
        return true
    }

    override fun random(): Word {
        return Word("TESTY")
    }

    override fun getWordForLevel(currentLevelNumber: Long): Word {
        TODO()
    }
}

class TestLevelRepository : LevelRepository {
    var currentLevel = 1L
    override fun getCurrentLevelNumber(): Long {
        return currentLevel
    }

    override fun levelPassed(level: Level) {
        currentLevel = level.number + 1
    }
}