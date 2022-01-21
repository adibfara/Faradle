package com.snakyapps.khiardle.backend.viewmodel

import com.snakyapps.khiardle.backend.models.Game
import com.snakyapps.khiardle.backend.models.Word
import com.snakyapps.khiardle.backend.usecase.GetWordStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class GameViewModel(
    private val initialGame: Game,
    private val getWordStatus: GetWordStatus,
) {
    data class State(
        val game: Game,
        val currentlyEnteringWord: String? = null,

        )

    private val stateFlow = MutableStateFlow<State>(State(
        initialGame,
    ))

    fun state(): StateFlow<State> = stateFlow

    private fun updateState(newState: State.() -> State) {
        stateFlow.value = newState(stateFlow.value)
    }

    fun characterEntered(character: Char) {
        val character = character.uppercaseChar()
        updateState {
            copy(currentlyEnteringWord = (currentlyEnteringWord ?: "") + character)
        }
    }

    fun backspacePressed() {
    }

    fun submit() {
    }
}