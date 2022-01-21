package com.snakyapps.khiardle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.snakyapps.khiardle.backend.models.EqualityStatus
import com.snakyapps.khiardle.backend.models.EqualityStatus.Correct
import com.snakyapps.khiardle.backend.models.EqualityStatus.Incorrect
import com.snakyapps.khiardle.backend.models.EqualityStatus.WrongPosition
import com.snakyapps.khiardle.backend.models.Game
import com.snakyapps.khiardle.backend.models.Guess
import com.snakyapps.khiardle.backend.models.Word
import com.snakyapps.khiardle.backend.models.WordStatus
import com.snakyapps.khiardle.backend.repository.AssetFileWordRepository
import com.snakyapps.khiardle.backend.usecase.GetWordStatus
import com.snakyapps.khiardle.backend.viewmodel.GameViewModel
import com.snakyapps.khiardle.ui.theme.KhiardleTheme
import com.snakyapps.khiardle.ui.theme.correctBackground
import com.snakyapps.khiardle.ui.theme.enteringBackground
import com.snakyapps.khiardle.ui.theme.incorrectBackground
import com.snakyapps.khiardle.ui.theme.wrongPositionBackground

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
                            Guess(
                                Word("TSETX"),
                                WordStatus.Incorrect(
                                    arrayOf(
                                        Correct,
                                        WrongPosition,
                                        WrongPosition,
                                        Correct,
                                        Incorrect,
                                    )
                                ),
                            ),
                            Guess(
                                Word("TESTY"), WordStatus.Correct,
                            )
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
    ) {
        Box(Modifier
            .fillMaxSize()
            .padding(64.dp)) {

            Column {
                GameGrid(state)
            }
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    private fun GameGrid(
        state: GameViewModel.State,
        modifier: Modifier = Modifier,
    ) {
        LazyVerticalGrid(cells = GridCells.Fixed(state.game.wordLength),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalArrangement = spacedBy(8.dp),
            modifier = modifier
        ) {
            val guesses = state.game.guesses.size
            items((state.game.wordLength * guesses)) {
                println(it)
                val guess = state.game.guesses[it / state.game.wordLength]
                val position = it % state.game.wordLength
                val character = guess.word.word[position]
                val status = when (guess.wordStatus) {
                    WordStatus.Correct -> Correct
                    is WordStatus.Incorrect -> guess.wordStatus.equalityStatuses[position]
                    WordStatus.NotExists -> TODO()
                }
                WordCharacterBox(character = character, status = status)

            }
            if (guesses < 6) {
                items(state.game.wordLength) {
                    val enteredCharacter = state.currentlyEnteringWord?.getOrNull(it)
                    if (enteredCharacter != null) {
                        WordCharacterBox(character = enteredCharacter, status = null)
                    } else {
                        EmptyCharacterBox()
                    }
                }

                val emptyBoxes =
                    (6 - guesses - 1).coerceAtLeast(
                        0)
                items(emptyBoxes * state.game.wordLength) {
                    EmptyCharacterBox()
                }
            }

        }
    }

    @Composable
    private fun WordCharacterBox(
        character: Char,
        status: EqualityStatus?,
        modifier: Modifier = Modifier,
    ) {
        val color = when (status) {
            WrongPosition -> MaterialTheme.colorScheme.wrongPositionBackground
            Correct -> MaterialTheme.colorScheme.correctBackground
            Incorrect -> MaterialTheme.colorScheme.incorrectBackground
            null -> MaterialTheme.colorScheme.enteringBackground
        }

        val textColor = when (status) {
            null -> MaterialTheme.colorScheme.onBackground
            else -> MaterialTheme.colorScheme.onPrimary
        }
        val borderModifier = if (status == null) Modifier.border(1.dp,
            MaterialTheme.colorScheme.incorrectBackground) else Modifier
        BasicCharacterBox(borderModifier, color, character, textColor, modifier)
    }

    @Composable
    fun EmptyCharacterBox(
        modifier: Modifier = Modifier,
    ) {
        BasicCharacterBox(modifier = modifier, borderModifier = Modifier.border(1.dp,
            MaterialTheme.colorScheme.incorrectBackground),
            color = Color.Transparent,
            character = null,
            textColor = Color.Transparent)
    }

    @Composable
    private fun BasicCharacterBox(
        borderModifier: Modifier,
        color: Color,
        character: Char?,
        textColor: Color,
        modifier: Modifier = Modifier,
    ) {
        Box(
            modifier
                .aspectRatio(1f)
                .clip(RoundedCornerShape(2.dp))
                .then(borderModifier)
                .background(animateColorAsState(targetValue = color).value),
            contentAlignment = Alignment.Center) {
            if (character != null)
                Text(character.uppercase(),
                    color = animateColorAsState(targetValue = textColor).value,
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black
                    ))
        }
    }

    @Preview
    @Composable
    private fun CharacterBoxPreview() {
        Row {
            WordCharacterBox(character = 'A', status = null)
            WordCharacterBox(character = 'D', status = Incorrect)
            WordCharacterBox(character = 'I', status = WrongPosition)
            WordCharacterBox(character = 'B', status = Correct)
        }
    }
}
