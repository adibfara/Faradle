package com.snakyapps.khiardle.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.snakyapps.khiardle.backend.models.WordStatus
import com.snakyapps.khiardle.backend.viewmodel.GameViewModel
import com.snakyapps.khiardle.ui.theme.correctBackground
import com.snakyapps.khiardle.ui.theme.enteringBackground
import com.snakyapps.khiardle.ui.theme.incorrectBackground
import com.snakyapps.khiardle.ui.theme.wrongPositionBackground

@OptIn(ExperimentalFoundationApi::class, ExperimentalAnimationApi::class)
@Composable
internal fun GameGrid(
    state: GameViewModel.State,
    modifier: Modifier = Modifier,
) {
    BoxWithConstraints(modifier = modifier) {

        Column(modifier = Modifier.fillMaxWidth()) {
            repeat(6) { row ->
                Row(Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp), horizontalArrangement = spacedBy(4.dp)) {
                    repeat(5) { column ->
                        val character: Char?
                        val status: EqualityStatus?
                        if (row < state.game.guesses.size) {
                            val guess = state.game.guesses[row]
                            character = guess.word.word[column]
                            status = when (guess.wordStatus) {
                                WordStatus.Correct -> EqualityStatus.Correct
                                is WordStatus.Incorrect -> guess.wordStatus.equalityStatuses[column]
                                WordStatus.NotExists -> EqualityStatus.Incorrect
                            }
                        } else {
                            character =
                                if (row == state.game.guesses.size) state.currentlyEnteringWord?.getOrNull(
                                    column) else null
                            status = null
                        }


                        WordCharacterBox(character = character,
                            status = status,
                            modifier = Modifier.weight(1f))
                    }

                }
            }
        }
    }
}

@Composable
internal fun WordCharacterBox(
    character: Char?,
    status: EqualityStatus?,
    modifier: Modifier = Modifier,
) {

    val color = when (status) {
        EqualityStatus.WrongPosition -> MaterialTheme.colorScheme.wrongPositionBackground
        EqualityStatus.Correct -> MaterialTheme.colorScheme.correctBackground
        EqualityStatus.Incorrect -> MaterialTheme.colorScheme.incorrectBackground
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
@OptIn(ExperimentalAnimationApi::class)
internal fun BasicCharacterBox(
    borderModifier: Modifier,
    color: Color,
    character: Char?,
    textColor: Color,
    modifier: Modifier = Modifier,
) {
    var lastChar by remember { mutableStateOf<Char?>(null) }
    if (character != null) {
        lastChar = character
    }
    Box(
        modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(2.dp))
            .then(borderModifier)
            .background(animateColorAsState(targetValue = color).value),
        contentAlignment = Alignment.Center) {
        AnimatedVisibility(character != null) {
            Text(lastChar?.uppercase() ?: "",
                color = animateColorAsState(targetValue = textColor).value,
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black
                ))
        }
    }
}

@Preview
@Composable
internal fun CharacterBoxPreview() {
    Row {
        WordCharacterBox(character = 'A', status = null)
        WordCharacterBox(character = 'D', status = EqualityStatus.Incorrect)
        WordCharacterBox(character = 'I', status = EqualityStatus.WrongPosition)
        WordCharacterBox(character = 'B', status = EqualityStatus.Correct)
    }
}