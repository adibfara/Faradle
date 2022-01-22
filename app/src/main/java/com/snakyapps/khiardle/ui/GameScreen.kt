package com.snakyapps.khiardle.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
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
    val viewModel = remember(word) {
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
        .padding(horizontal = 32.dp, vertical = 16.dp)) {

        Column {
            val context = LocalContext.current
            Column(Modifier
                .clickable {

                    context.startActivity(Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse("http://www.github.com/adibfara/khiardle/")
                    })
                }
                .align(CenterHorizontally)) {

                Text(text = "Khiardle",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Serif,
                    modifier = Modifier.align(
                        CenterHorizontally))
                Text(text = "github.com/adibfara/khiardle",

                    fontSize = 10.sp,
                    modifier = Modifier.align(
                        CenterHorizontally))
            }

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
    }
}

@Composable
private fun BoxScope.WonScreen(
    state: GameViewModel.State,
    shownWon: () -> Unit,
) {

    AnimatedVisibility(state.game.isWon, modifier = Modifier.align(Center)
    ) {
        Box(Modifier.Companion
            .align(Alignment.BottomCenter)
            .clip(RoundedCornerShape(5.dp))
            .background(MaterialTheme.colorScheme.primary)
            .clickable {
                shownWon()
            }
            .padding(84.dp), Center) {
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