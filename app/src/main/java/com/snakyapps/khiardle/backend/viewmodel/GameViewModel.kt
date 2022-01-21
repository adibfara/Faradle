package com.snakyapps.khiardle.backend.viewmodel

import com.snakyapps.khiardle.backend.models.Game
import com.snakyapps.khiardle.backend.models.Guess
import com.snakyapps.khiardle.backend.models.Word
import com.snakyapps.khiardle.backend.models.WordStatus
import com.snakyapps.khiardle.backend.usecase.GetWordStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class GameViewModel(
    private val initialGame: Game,
    private val getWordStatus: GetWordStatus,
) {
    data class State(
        val game: Game,
        val currentlyEnteringWord: String? = null,
        val doesNotExist: Boolean = false,
    )

    private val stateFlow = MutableStateFlow<State>(State(
        initialGame,
    ))

    fun state(): StateFlow<State> = stateFlow

    private fun updateState(newState: State.() -> State) {
        stateFlow.value = newState(stateFlow.value)
    }

    private fun currentState(): State = state().value

    fun characterEntered(character: Char) {
        if (wordIsEnteredCompletely()) return
        val character = character.uppercaseChar()
        updateState {
            copy(currentlyEnteringWord = (currentlyEnteringWord ?: "") + character)
        }
    }

    private fun wordIsEnteredCompletely() =
        currentState().currentlyEnteringWord?.length == currentState().game.wordLength

    fun backspacePressed() {
        updateState {
            val newWord = when {
                currentlyEnteringWord == null -> null
                currentlyEnteringWord.length == 1 -> null
                else -> currentlyEnteringWord.dropLast(1)
            }
            copy(currentlyEnteringWord = newWord)
        }
    }

    fun submit() {
        if (!wordIsEnteredCompletely()) return
        val word = Word(currentState().currentlyEnteringWord!!)
        val status = getWordStatus.execute(word,
            currentState().game.originalWord)
        updateState {
            copy(
                game = game.copy(guesses = game.guesses + Guess(
                    word, status
                )),
                currentlyEnteringWord = if (status != WordStatus.NotExists) null else currentlyEnteringWord,
                doesNotExist = if (status == WordStatus.NotExists) true else doesNotExist)
        }
    }

    fun shownNotExists() {
        updateState { copy(doesNotExist = false) }
    }
}