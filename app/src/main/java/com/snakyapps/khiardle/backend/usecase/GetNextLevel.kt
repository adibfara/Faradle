package com.snakyapps.khiardle.backend.usecase

import com.snakyapps.khiardle.backend.models.Level
import com.snakyapps.khiardle.backend.repository.LevelRepository
import com.snakyapps.khiardle.backend.repository.WordRepository

class GetNextLevel(
    private val wordRepository: WordRepository,
    private val levelRepository: LevelRepository,
) {
    fun execute(): Level {
        val currentLevelNumber = levelRepository.getCurrentLevelNumber()
        return wordRepository.getWordForLevel(currentLevelNumber).let { word ->
            Level(currentLevelNumber, word)
        }
    }
}