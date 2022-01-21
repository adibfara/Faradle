package com.snakyapps.khiardle.backend.usecase

import com.snakyapps.khiardle.backend.models.EqualityStatus
import com.snakyapps.khiardle.backend.models.EqualityStatus.*
import com.snakyapps.khiardle.backend.models.Word
import com.snakyapps.khiardle.backend.models.WordStatus
import com.snakyapps.khiardle.backend.repository.WordRepository
import com.snakyapps.khiardle.fixtures.AllExistRepository
import com.snakyapps.khiardle.fixtures.TestWordRepository
import org.junit.Assert.assertEquals
import org.junit.Test

class GetWordStatusTest {
    private val testWordRepository = TestWordRepository()

    private fun createUseCase(wordRepository: WordRepository = testWordRepository) =
        GetWordStatus(wordRepository)

    private val original = Word("TITLE")

    @Test
    fun `when the word does not exist, output is NotExists`() {
        val useCase = createUseCase()
        assertEquals(WordStatus.NotExists, useCase.execute(Word("NOTES"), original))
    }

    @Test
    fun `when the word is correct, output is Correct`() {
        val useCase = createUseCase()
        assertEquals(WordStatus.Correct, useCase.execute(original, original))
    }


    @Test
    fun `when all characers are correct except one, output is correct`() {

        val title = "TITLE"
        assertWord(
            title, "TITLL", arrayOf(
                Correct, Correct, Correct, Correct, Incorrect
            )
        )
        assertWord(
            title, "TITLL", arrayOf(
                Correct, Correct, Correct, Correct, Incorrect
            )
        )

        assertWord(
            title, "TIELT", arrayOf(
                Correct, Correct, WrongPosition, Correct, WrongPosition
            )
        )

        assertWord(
            title, "XXXXE", arrayOf(
                Incorrect, Incorrect, Incorrect, Incorrect, Correct
            )
        )

        assertWord(
            title, "XXXXT", arrayOf(
                Incorrect, Incorrect, Incorrect, Incorrect, WrongPosition
            )
        )
        assertWord(
            title, "ITLET", arrayOf(
                WrongPosition, WrongPosition, WrongPosition, WrongPosition, WrongPosition
            )
        )
        assertWord(
            "TTFFF", "XXTTT", arrayOf(
                Incorrect, Incorrect, WrongPosition, WrongPosition, Incorrect
            )
        )

    }

    private fun assertWord(original: String, checking: String, expected: Array<EqualityStatus>) {
        val useCase = createUseCase(AllExistRepository())
        val original = Word(original)
        assertEquals(
            "for ${original.word}, checking $checking",
            WordStatus.Incorrect(
                expected
            ), useCase.execute(Word(checking), original)
        )
    }
}
