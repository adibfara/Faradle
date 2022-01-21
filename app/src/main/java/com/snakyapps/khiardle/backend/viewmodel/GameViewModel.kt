package com.snakyapps.khiardle.backend.viewmodel

import com.snakyapps.khiardle.backend.models.Game
import com.snakyapps.khiardle.backend.models.Word
import com.snakyapps.khiardle.backend.usecase.GetWordStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

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

    fun state(): Flow<State> = stateFlow
    fun wordEntered(word: Word) {
    }

    fun backspacePressed() {
    }
}