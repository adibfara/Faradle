package com.snakyapps.khiardle.backend.repository

import android.content.res.AssetManager
import com.snakyapps.khiardle.backend.models.Word

class AssetFileWordRepository(private val assetManager: AssetManager) : WordRepository {
    private val allWords =
        assetManager.open("words.txt").readBytes().decodeToString().split("\r\n", "\n")
    override fun find(word: Word): Boolean {
        return allWords.contains(word.word.lowercase())
    }
}