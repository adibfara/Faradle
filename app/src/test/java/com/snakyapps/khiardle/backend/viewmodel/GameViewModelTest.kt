package com.snakyapps.khiardle.backend.viewmodel

import com.snakyapps.khiardle.backend.models.Game
import com.snakyapps.khiardle.backend.models.Word
import com.snakyapps.khiardle.backend.models.WordStatus
import com.snakyapps.khiardle.backend.usecase.GetWordStatus
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.After
import org.junit.Before

import org.junit.Assert.*
import org.junit.Test

class GameViewModelTest {

    @RelaxedMockK
    lateinit var getWordStatus: GetWordStatus

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @After
    fun tearDown() {
    }

    private val testGame = Game(
        Word("TESTY"),
        listOf(

        ),
        5
    )

    private fun createViewModel(
        game: Game = testGame,
    ) = GameViewModel(testGame, getWordStatus)

    private fun Word.mockStatus(wordStatus: WordStatus) {
        coEvery {
            getWordStatus.execute(this@mockStatus, any())
        } returns wordStatus
    }

    private fun GameViewModel.appendTestWord(word: String) {
        word.forEach {
            characterEntered(it)
        }
    }

    @Test
    fun `when word is entered, it is added to state`() {
        val viewModel = createViewModel()
        assertEquals(GameViewModel.State(testGame, null), viewModel.state().value)

        viewModel.characterEntered('a')
        assertEquals(GameViewModel.State(testGame, "A"), viewModel.state().value)
        viewModel.appendTestWord("bbyl")

        assertEquals(GameViewModel.State(testGame, "ABBYL"), viewModel.state().value)
    }
}