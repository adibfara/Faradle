package com.snakyapps.khiardle.backend.viewmodel

import com.snakyapps.khiardle.backend.models.EqualityStatus
import com.snakyapps.khiardle.backend.models.Game
import com.snakyapps.khiardle.backend.models.Guess
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
        testGame: Game = this@GameViewModelTest.testGame,
    ) = GameViewModel(testGame, getWordStatus)

    private fun Word.mockStatus(wordStatus: WordStatus): Word {
        coEvery {
            getWordStatus.execute(this@mockStatus, any())
        } returns wordStatus

        return this
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

    @Test
    fun `word length is considered`() {
        val testGame = testGame.copy(wordLength = 3)
        val viewModel = createViewModel(testGame)
        viewModel.appendTestWord("abcsdlkfj")

        assertEquals(GameViewModel.State(testGame, "ABC"), viewModel.state().value)
    }

    @Test
    fun `submit does not work until word is entered completely`() {
        val viewModel = createViewModel()
        viewModel.characterEntered('a')
        viewModel.submit()
        assertEquals(GameViewModel.State(testGame, "A"), viewModel.state().value)
    }

    @Test
    fun `submit tests the word and adds it to game when it exists`() {
        val viewModel = createViewModel()
        val wordStatus1 = WordStatus.Incorrect(arrayOf(
            EqualityStatus.Correct,
            EqualityStatus.Correct,
            EqualityStatus.Correct,
            EqualityStatus.Correct,
            EqualityStatus.Incorrect
        ))
        val wordStatus2 = WordStatus.Incorrect(arrayOf(
            EqualityStatus.Correct,
            EqualityStatus.WrongPosition,
            EqualityStatus.WrongPosition,
            EqualityStatus.Correct,
            EqualityStatus.Incorrect
        ))

        viewModel.assertGuesses(
            Word("testa").mockStatus(wordStatus1),
            listOf(wordStatus1))
        viewModel.assertGuesses(
            Word("tsetx").mockStatus(wordStatus2),
            listOf(wordStatus1, wordStatus2))
    }

    private fun GameViewModel.assertGuesses(
        testWord: Word,
        guessStatuses: List<WordStatus>,
    ) {
        val guesses = guessStatuses.map {
            Guess(testWord, it)
        }
        appendTestWord(testWord.word)
        submit()
        assertEquals(GameViewModel.State(testGame.copy(
            guesses = guesses
        ), null), state().value)
    }
}