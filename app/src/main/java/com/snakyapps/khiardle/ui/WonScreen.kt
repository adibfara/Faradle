package com.snakyapps.khiardle.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.snakyapps.khiardle.backend.viewmodel.GameViewModel

@OptIn(ExperimentalAnimationApi::class)
@Composable
internal fun BoxScope.WonScreen(
    state: GameViewModel.State,
    shownWon: () -> Unit,
) {

    AnimatedVisibility(state.game.isWon, modifier = Modifier.align(Alignment.Center)
    ) {
        Box(Modifier
            .align(Alignment.BottomCenter)
            .clip(RoundedCornerShape(5.dp))
            .background(MaterialTheme.colorScheme.primary)
            .clickable {
                shownWon()
            }
            .padding(84.dp), Alignment.Center) {
            Text(text = "You WON!",
                color = MaterialTheme.colorScheme.onPrimary)
        }
    }
}