package com.snakyapps.khiardle.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.snakyapps.khiardle.backend.models.EqualityStatus
import com.snakyapps.khiardle.backend.models.KeyboardKeys
import com.snakyapps.khiardle.backend.viewmodel.GameViewModel
import com.snakyapps.khiardle.ui.theme.correctBackground
import com.snakyapps.khiardle.ui.theme.keyboard
import com.snakyapps.khiardle.ui.theme.keyboardDisabled
import com.snakyapps.khiardle.ui.theme.onKeyboard
import com.snakyapps.khiardle.ui.theme.wrongPositionBackground

@Composable
internal fun GameKeyboard(
    state: GameViewModel.State,
    onKey: (char: Char) -> Unit,
    onBackspace: () -> Unit,
    onSubmit: () -> Unit,
) {
    BoxWithConstraints() {
        Column {
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxWidth()) {

                repeat(10) {
                    val key = state.game.availableKeyboard.keys[it]
                    KeyboardKey(key, onKey, Modifier.weight(1f))
                }
            }
            Spacer(Modifier.size(4.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.padding(start = 8.dp)) {

            repeat(9) {
                val key = state.game.availableKeyboard.keys[10 + it]
                KeyboardKey(key, onKey, Modifier.weight(1f))
            }
        }
        Spacer(Modifier.size(4.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier) {

            KeyboardKey(text = " ENTER ", onClick = onSubmit)
            repeat(7) {
                val key = state.game.availableKeyboard.keys[19 + it]
                KeyboardKey(key, onKey, Modifier.weight(1f))
            }

            KeyboardKey(text = "⌫",
                modifier = Modifier.width(64.dp),
                onClick = onBackspace)
        }
        }
    }
}

@Composable
private fun KeyboardKey(
    key: KeyboardKeys.Key,
    onKey: (char: Char) -> Unit,
    modifier: Modifier = Modifier,
) {
    KeyboardKey(key.button.toString().uppercase(), modifier = modifier, key.equalityStatus) {
        onKey(key.button)
    }
}

@Composable
private fun KeyboardKey(
    text: String,
    modifier: Modifier = Modifier,
    status: EqualityStatus? = null,
    onClick: () -> Unit,
) {
    val color by animateColorAsState(targetValue = when (status) {
        EqualityStatus.Incorrect -> MaterialTheme.colorScheme.keyboardDisabled
        else -> MaterialTheme.colorScheme.keyboard
    })
    val testColor by animateColorAsState(targetValue = when (status) {
        EqualityStatus.WrongPosition -> MaterialTheme.colorScheme.wrongPositionBackground
        EqualityStatus.Correct -> MaterialTheme.colorScheme.correctBackground
        EqualityStatus.Incorrect, null -> MaterialTheme.colorScheme.onKeyboard
    })
    Box(modifier
        .height(40.dp)
        .clip(RoundedCornerShape(2.dp))
        .background(color)
        .clickable(onClick = onClick), Alignment.Center) {
        Text(
            modifier = Modifier,
            text = text,
            color = testColor,
            fontSize = 18.sp
        )
    }
}