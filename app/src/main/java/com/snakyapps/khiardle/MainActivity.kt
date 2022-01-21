package com.snakyapps.khiardle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.unit.sp
import com.snakyapps.khiardle.backend.models.Game
import com.snakyapps.khiardle.backend.models.KeyboardKeys
import com.snakyapps.khiardle.backend.models.Word
import com.snakyapps.khiardle.backend.repository.AssetFileWordRepository
import com.snakyapps.khiardle.backend.usecase.GetWordStatus
import com.snakyapps.khiardle.backend.viewmodel.GameViewModel
import com.snakyapps.khiardle.ui.GameGrid
import com.snakyapps.khiardle.ui.theme.KhiardleTheme
import com.snakyapps.khiardle.ui.theme.keyboard
import com.snakyapps.khiardle.ui.theme.keyboardDisabled
import com.snakyapps.khiardle.ui.theme.onKeyboard
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KhiardleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val assetWordRepository = remember {
                        AssetFileWordRepository(assets)
                    }
                    val getWordStatus = remember {
                        GetWordStatus(assetWordRepository)
                    }
                    val viewModel = remember {
                        val initialGame = Game(Word("TESTY"), listOf(

                        ), 5)
                        GameViewModel(initialGame, getWordStatus)
                    }

                    val state by viewModel.state().collectAsState()
                    Game(state, onKey = {
                        viewModel.characterEntered(it)
                    },
                        onBackspace = {
                            viewModel.backspacePressed()
                        },
                        onSubmit = {
                            viewModel.submit()
                        }, shownError = {
                            viewModel.shownNotExists()
                        })
                }
            }
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun Game(
        state: GameViewModel.State,
        onKey: (char: Char) -> Unit,
        onBackspace: () -> Unit,
        onSubmit: () -> Unit,
        shownError: () -> Unit,
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
            LaunchedEffect(key1 = state.doesNotExist, block = {
                if (state.doesNotExist) {
                    delay(2000)
                    shownError()
                }
            })
            AnimatedVisibility(state.doesNotExist, modifier = Modifier
                .align(Alignment.BottomCenter)) {
                Box(Modifier
                    .align(Alignment.BottomCenter)
                    .clip(RoundedCornerShape(5.dp))
                    .background(MaterialTheme.colorScheme.error)
                    .padding(16.dp)) {
                    Text(text = "The word does not exist!",
                        color = MaterialTheme.colorScheme.onError)
                }
            }
        }
    }

    @Composable
    internal fun GameKeyboard(
        state: GameViewModel.State,
        onKey: (char: Char) -> Unit,
        onBackspace: () -> Unit,
        onSubmit: () -> Unit,
    ) {
        val modifier = remember {
            Modifier.width(32.dp)
        }
        Column {
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {

                repeat(10) {
                    val key = state.game.availableKeyboard.keys[it]
                    KeyboardKey(key, onKey, modifier)
                }
            }
            Spacer(Modifier.size(4.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.padding(start = 8.dp)) {

                repeat(9) {
                    val key = state.game.availableKeyboard.keys[10 + it]
                    KeyboardKey(key, onKey, modifier)
                }
            }
            Spacer(Modifier.size(4.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier) {

                KeyboardKey(text = "ENTER", isEnabled = true, onClick = onSubmit)
                repeat(7) {
                    val key = state.game.availableKeyboard.keys[19 + it]
                    KeyboardKey(key, onKey, modifier)
                }

                KeyboardKey(text = "⌫",
                    isEnabled = true,
                    onClick = onBackspace,
                    modifier = Modifier.width(64.dp))
            }
        }
    }

    @Composable
    private fun KeyboardKey(
        key: KeyboardKeys.Key,
        onKey: (char: Char) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        KeyboardKey(key.button.toString().uppercase(), key.enabled, modifier = modifier) {
            onKey(key.button)
        }
    }

    @Composable
    private fun KeyboardKey(
        text: String,
        isEnabled: Boolean,
        modifier: Modifier = Modifier,
        onClick: () -> Unit,
    ) {
        val color by animateColorAsState(targetValue = if (isEnabled) MaterialTheme.colorScheme.keyboard else MaterialTheme.colorScheme.keyboardDisabled)
        Box(modifier
            .height(40.dp)
            .clip(RoundedCornerShape(2.dp))
            .background(color)
            .clickable(onClick = onClick), Alignment.Center) {
            Text(
                modifier = Modifier,
                text = text,
                color = MaterialTheme.colorScheme.onKeyboard,
                fontSize = 18.sp
            )
        }
    }
}
