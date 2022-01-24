package com.snakyapps.khiardle.backend.viewmodel

import com.snakyapps.khiardle.backend.models.Level
import com.snakyapps.khiardle.backend.repository.LevelRepository
import com.snakyapps.khiardle.backend.repository.WordRepository
import com.snakyapps.khiardle.backend.usecase.GetNextLevel
import com.snakyapps.khiardle.fixtures.TestLevelRepository
import com.snakyapps.khiardle.fixtures.TestWordRepository
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class LevelsViewModelTest {
    private lateinit var testWordRepository: WordRepository
    private lateinit var testLevelRepository: LevelRepository
    private lateinit var getNextLevel: GetNextLevel

    @Before
    fun setUp() {
        testWordRepository = TestWordRepository()
        testLevelRepository = TestLevelRepository()
        getNextLevel = GetNextLevel(testWordRepository, testLevelRepository)
    }

    private fun createViewModel() = LevelsViewModel(
        testLevelRepository, getNextLevel
    )

    @Test
    fun `getting next level gets the next word and creates a level`() {
        val viewModel = createViewModel()
        val level1 = Level(1, testWordRepository.getWordForLevel(1))
        val level2 = Level(2, testWordRepository.getWordForLevel(2))
        assertEquals(level1, viewModel.currentState().currentLevel)
        viewModel.levelPassed()
        assertEquals(level2, viewModel.currentState().currentLevel)
    }
}