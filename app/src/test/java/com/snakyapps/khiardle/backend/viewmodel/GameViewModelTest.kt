package com.snakyapps.khiardle.backend.viewmodel

import com.snakyapps.khiardle.backend.models.EqualityStatus
import com.snakyapps.khiardle.backend.models.Game
import com.snakyapps.khiardle.backend.models.Guess
import com.snakyapps.khiardle.backend.models.Word
import com.snakyapps.khiardle.backend.models.WordStatus
import com.snakyapps.khiardle.backend.usecase.GetWordStatus
import com.snakyapps.khiardle.fixtures.TestWordRepository
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import org.junit.After
import org.junit.Before

import org.junit.Assert.*
import org.junit.Test

class GameViewModelTest {

    lateinit var testWordRepository: TestWordRepository
    lateinit var getWordStatus: GetWordStatus

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        testWordRepository = TestWordRepository()
        getWordStatus = GetWordStatus(testWordRepository)
    }

    @After
    fun tearDown() {
        clearAllMocks()
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

    private fun Word.mockStatus(): Word {
        testWordRepository.addWord(this)
        return this
    }

    private fun GameViewModel.appendTestWord(word: String) {
        word.forEach {
            characterEntered(it)
        }
    }

    private fun GameViewModel.assertGuesses(
        testWord: Word,
        guesses: List<Guess>,
    ) {
        val testWord = Word(testWord.word.uppercase())

        appendTestWord(testWord.word)
        submit()
        assertEquals(GameViewModel.State(testGame.copy(
            guesses = guesses
        ), null), state().value)
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

        val word1 = Word("TESTA").mockStatus()
        val wordStatus1 = WordStatus.Incorrect(arrayOf(
            EqualityStatus.Correct,
            EqualityStatus.Correct,
            EqualityStatus.Correct,
            EqualityStatus.Correct,
            EqualityStatus.Incorrect
        ))

        viewModel.assertGuesses(
            word1,
            listOf(Guess(word1, wordStatus1)))

        val word2 = Word("TSETX").mockStatus()
        val wordStatus2 = WordStatus.Incorrect(arrayOf(
            EqualityStatus.Correct,
            EqualityStatus.WrongPosition,
            EqualityStatus.WrongPosition,
            EqualityStatus.Correct,
            EqualityStatus.Incorrect
        ))

        viewModel.assertGuesses(
            word2,
            listOf(Guess(word1, wordStatus1), Guess(word2, wordStatus2)))

        val word3 = Word("TESTY").mockStatus()

        val wordStatus3 = WordStatus.Correct

        viewModel.assertGuesses(
            word3,
            listOf(Guess(word1, wordStatus1), Guess(word2, wordStatus2), Guess(word3, wordStatus3)))
    }

    @Test
    fun `submitting a word that does not exists does not add a guess`() {
        val viewModel = createViewModel(testGame)
        viewModel.appendTestWord("abcsdlkfj")

        viewModel.submit()
        assertEquals(true, viewModel.state().value.doesNotExist)
        viewModel.shownNotExists()
        assertEquals(false, viewModel.state().value.doesNotExist)
        assertEquals("ABCSD", viewModel.state().value.currentlyEnteringWord)
        assertEquals(0, viewModel.state().value.game.guesses.size)
    }

    @Test
    fun `backspace works correctly`() {
        val viewModel = createViewModel()
        assertEquals(GameViewModel.State(testGame, null), viewModel.state().value)
        viewModel.backspacePressed()
        assertEquals(null, viewModel.state().value.currentlyEnteringWord)
        viewModel.characterEntered('a')
        viewModel.backspacePressed()
        assertEquals(null, viewModel.state().value.currentlyEnteringWord)
        viewModel.appendTestWord("bbyl")
        viewModel.backspacePressed()
        viewModel.backspacePressed()
        assertEquals("BB", viewModel.state().value.currentlyEnteringWord)
    }
}