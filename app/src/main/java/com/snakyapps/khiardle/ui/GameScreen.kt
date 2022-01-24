package com.snakyapps.khiardle.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.snakyapps.khiardle.backend.models.Game
import com.snakyapps.khiardle.backend.models.Level
import com.snakyapps.khiardle.backend.usecase.GetWordStatus
import com.snakyapps.khiardle.backend.viewmodel.GameViewModel

@Composable
internal fun WordScreen(
    level: Level,
    getWordStatus: GetWordStatus,
    levelCompleted: () -> Unit,
) {
    val word = level.word
    val viewModel = remember(word) {
        val initialGame = Game(word, listOf(
        ), 5)
        GameViewModel(initialGame, getWordStatus)
    }

    val state by viewModel.state().collectAsState()
    GameScreen(level, state, onKey = {
        viewModel.characterEntered(it)
    },
        onBackspace = {
            viewModel.backspacePressed()
        },
        onSubmit = {
            viewModel.submit()
        }, shownError = {
            viewModel.shownNotExists()
        },
        shownWon = levelCompleted,
        shownLost = {
            viewModel.shownLost()
        })
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GameScreen(
    level: Level,
    state: GameViewModel.State,
    onKey: (char: Char) -> Unit,
    onBackspace: () -> Unit,
    onSubmit: () -> Unit,
    shownError: () -> Unit,
    shownWon: () -> Unit,
    shownLost: () -> Unit,
) {

    Box(Modifier
        .fillMaxSize()
        .padding(horizontal = 32.dp, vertical = 16.dp)) {

        Column {
            GameHeader(level)

            GameGrid(state,
                modifier = Modifier
                    .padding(top = 32.dp)
                    .weight(1f)
                    .fillMaxWidth(0.7f)
                    .align(CenterHorizontally))
            Spacer(modifier = Modifier.size(16.dp))
            GameKeyboard(
                state,
                onKey = onKey,
                onBackspace = onBackspace,
                onSubmit = onSubmit,
            )
        }
        ErrorScreen(state, shownError)
        WonScreen(state, shownWon)
        GameOverScreen(state, shownLost)
    }
}

