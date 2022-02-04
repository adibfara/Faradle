package com.snakyapps.khiardle.backend.repository

import android.content.res.AssetManager
import com.snakyapps.khiardle.backend.models.Word

class AssetFileWordRepository(assetManager: AssetManager) : WordRepository {
    private val allWords =
        assetManager.open("words.txt").readBytes().decodeToString().split("\r\n", "\n")
            .filter { it.length == 5 }.map { it.uppercase().trim() }.toSet()
    private val wordsForLevels =
        assetManager.open("top.txt").readBytes().decodeToString().split("\r\n", "\n")
            .filter { it.length == 5 }.map { it.uppercase() }.toList()
    override fun find(word: Word): Boolean {
        return allWords.contains(word.word.uppercase().trim())
    }

    override fun random(): Word {
        return Word(allWords.random())
    }

    override fun getWordForLevel(currentLevelNumber: Long): Word {
        return Word(wordsForLevels[currentLevelNumber.toInt() - 1])
    }

    override val lastLevel: Long
        get() = 1024
}