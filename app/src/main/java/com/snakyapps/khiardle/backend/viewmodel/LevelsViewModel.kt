package com.snakyapps.khiardle.backend.viewmodel

import com.snakyapps.khiardle.backend.models.Level
import com.snakyapps.khiardle.backend.repository.AssetFileWordRepository
import com.snakyapps.khiardle.backend.repository.LevelRepository
import com.snakyapps.khiardle.backend.repository.WordRepository
import com.snakyapps.khiardle.backend.usecase.GetNextLevel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LevelsViewModel(
    private val levelRepository: LevelRepository,
    private val getNextLevel: GetNextLevel,
) : BaseViewModel<LevelsViewModel.State>(State()) {
    data class State(
        val currentLevel: Level? = null,
    )

    init {
        updateLevel()
    }

    fun levelPassed() {
        currentState().currentLevel?.let { levelRepository.levelPassed(it) }
        updateLevel()
    }

    private fun updateLevel() {
        updateState {
            copy(currentLevel = getNextLevel.execute())
        }
    }
}