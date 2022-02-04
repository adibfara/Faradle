package com.snakyapps.khiardle.backend.usecase

import com.snakyapps.khiardle.backend.models.Level
import com.snakyapps.khiardle.backend.repository.LevelRepository
import com.snakyapps.khiardle.backend.repository.WordRepository

class ResetLevels(
    private val levelRepository: LevelRepository,
) {
    fun execute() {
        levelRepository.reset()
    }
}