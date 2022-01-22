package com.snakyapps.khiardle.backend.repository

import android.content.res.AssetManager
import com.snakyapps.khiardle.backend.models.Word
import java.util.stream.Collectors.toSet

class AssetFileWordRepository(assetManager: AssetManager) : WordRepository {
    private val allWords =
        assetManager.open("words.txt").readBytes().decodeToString().split("\r\n", "\n")
            .filter { it.length == 5 }.map { it.uppercase() }.toSet()

    override fun find(word: Word): Boolean {
        return allWords.contains(word.word.uppercase())
    }

    override fun random(): Word {
        return Word(allWords.random())
    }
}