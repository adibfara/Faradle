package com.snakyapps.khiardle.backend.models

import com.snakyapps.khiardle.backend.models.GameKtTest.TestKeyboard.Companion.allKeys
import com.snakyapps.khiardle.backend.models.GameKtTest.TestKeyboard.Companion.default
import org.junit.Assert.*
import org.junit.Test

class GameKtTest {

    data class TestKeyboard(override val keys: List<Key>) : KeyboardKeys(
        keys,
        Language.English
    ) {
        override fun withUpdatedButton(keys: List<Key>): KeyboardKeys {
            return copy(keys = keys)
        }

        companion object {
            val allKeys = listOf(
                Key('A', true),
                Key('B', true),
                Key('C', true),
                Key('D', true),
                Key('E', true),
                Key('F', true),
                Key('G', true),
            )

            fun default(): TestKeyboard {

                return TestKeyboard(
                    allKeys
                )
            }
        }
    }

    private val testGame = Game(
        Word("TESTY"),
        listOf(

        ),
        5,
        default()
    )

    @Test
    fun `when game is won it is included in the state`() {
        val game = testGame
        assertEquals(false, game.isWon)
        val wonGame = testGame.copy(guesses = listOf(Guess(Word("TESTY"), WordStatus.Correct)))
        assertEquals(true, wonGame.isWon)
    }

    @Test
    fun `when character is disabled, it is not enabled in the keyboard`() {
        val game = testGame
        assertEquals(allKeys, game.availableKeyboard.keys)

        val game2 = testGame.copy(guesses = listOf(
            Guess(
                Word("ABC"),
                WordStatus.Incorrect(
                    arrayOf(
                        EqualityStatus.Correct,
                        EqualityStatus.WrongPosition,
                        EqualityStatus.Incorrect
                    )
                )
            ),
            Guess(
                Word("AFB"),
                WordStatus.Incorrect(
                    arrayOf(
                        EqualityStatus.Correct, EqualityStatus.WrongPosition, EqualityStatus.Correct
                    )
                )
            ),
            Guess(
                Word("DED"),
                WordStatus.Incorrect(
                    arrayOf(
                        EqualityStatus.Incorrect, EqualityStatus.Incorrect, EqualityStatus.Incorrect
                    )
                )
            ),
        ))
        assertEquals(listOf(
            KeyboardKeys.Key('A', true),
            KeyboardKeys.Key('B', true),
            KeyboardKeys.Key('C', false),
            KeyboardKeys.Key('D', false),
            KeyboardKeys.Key('E', false),
            KeyboardKeys.Key('F', true),
            KeyboardKeys.Key('G', true),
        ), game2.availableKeyboard.keys)
    }
}