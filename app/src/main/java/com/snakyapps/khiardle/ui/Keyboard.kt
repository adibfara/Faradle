package com.snakyapps.khiardle.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.snakyapps.khiardle.backend.models.KeyboardKeys
import com.snakyapps.khiardle.backend.viewmodel.GameViewModel
import com.snakyapps.khiardle.ui.theme.keyboard
import com.snakyapps.khiardle.ui.theme.keyboardDisabled
import com.snakyapps.khiardle.ui.theme.onKeyboard

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