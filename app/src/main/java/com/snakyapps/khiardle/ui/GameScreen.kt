package com.snakyapps.khiardle.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.snakyapps.khiardle.backend.models.Game
import com.snakyapps.khiardle.backend.models.Word
import com.snakyapps.khiardle.backend.usecase.GetWordStatus
import com.snakyapps.khiardle.backend.viewmodel.GameViewModel
import kotlinx.coroutines.delay

@Composable
internal fun WordScreen(
    word: Word,
    getWordStatus: GetWordStatus,
    shownWon: () -> Unit,
) {
    val viewModel = remember {
        val initialGame = Game(word, listOf(

        ), 5)
        GameViewModel(initialGame, getWordStatus)
    }

    val state by viewModel.state().collectAsState()
    GameScreen(state, onKey = {
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
        shownWon = shownWon)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GameScreen(
    state: GameViewModel.State,
    onKey: (char: Char) -> Unit,
    onBackspace: () -> Unit,
    onSubmit: () -> Unit,
    shownError: () -> Unit,
    shownWon: () -> Unit,
) {

    Box(Modifier
        .fillMaxSize()
        .padding(32.dp)) {

        Column {
            GameGrid(state)
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
    }
}

@Composable
private fun BoxScope.WonScreen(
    state: GameViewModel.State,
    shownWon: () -> Unit,
) {
    LaunchedEffect(key1 = state.game.isWon, block = {
        if (state.game.isWon) {
            delay(3000)
            shownWon()
        }
    })
    AnimatedVisibility(state.game.isWon, modifier = Modifier
        .fillMaxSize()) {
        Box(Modifier.Companion
            .align(Alignment.BottomCenter)
            .clip(RoundedCornerShape(5.dp))
            .background(MaterialTheme.colorScheme.primary)
            .padding(16.dp)) {
            Text(text = "You WON!",
                color = MaterialTheme.colorScheme.onPrimary)
        }
    }
}

@Composable
private fun BoxScope.ErrorScreen(
    state: GameViewModel.State,
    shownError: () -> Unit,
) {
    LaunchedEffect(key1 = state.doesNotExist, block = {
        if (state.doesNotExist) {
            delay(2000)
            shownError()
        }
    })
    AnimatedVisibility(state.doesNotExist, modifier = Modifier.Companion
        .align(Alignment.BottomCenter)) {
        Box(Modifier.Companion
            .align(Alignment.BottomCenter)
            .clip(RoundedCornerShape(5.dp))
            .background(MaterialTheme.colorScheme.error)
            .padding(16.dp)) {
            Text(text = "The word does not exist!",
                color = MaterialTheme.colorScheme.onError)
        }
    }
}