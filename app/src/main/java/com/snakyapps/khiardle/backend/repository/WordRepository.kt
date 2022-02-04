package com.snakyapps.khiardle.backend.repository

import com.snakyapps.khiardle.backend.models.Word

interface WordRepository {
    val lastLevel: Long
    fun find(word: Word): Boolean
    fun random(): Word
    fun getWordForLevel(currentLevelNumber: Long): Word
}

