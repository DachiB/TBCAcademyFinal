package com.example.tbcacademyfinal.presentation.ui.tutorial

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tbcacademyfinal.common.CollectSideEffect
import com.example.tbcacademyfinal.presentation.theme.CreamWhite
import com.example.tbcacademyfinal.presentation.theme.GreenVariant
import com.example.tbcacademyfinal.presentation.ui.main.collection.CollectionSideEffect
import kotlinx.coroutines.delay

@Composable
fun TutorialScreen(
    viewModel: TutorialViewModel = hiltViewModel(),
    onNavigateToLogin: () -> Unit,
    onNavigateContinueTutorial: () -> Unit,
) {
    CollectSideEffect(flow = viewModel.event) { effect ->
        when (effect) {
            is TutorialSideEffect.NavigateToLogin -> onNavigateToLogin()
            is TutorialSideEffect.NavigateToNextTutorial -> onNavigateContinueTutorial()
        }
    }
    TutorialContent(onIntent = viewModel::processIntent)
}

@Composable
fun TutorialContent(
    onIntent: (TutorialIntent) -> Unit
) {
    var contentVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(500)
        contentVisible = true
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),

        ) {
        AnimatedVisibility(
            visible = contentVisible,
            modifier = Modifier.weight(1f),
            enter = fadeIn(animationSpec = tween(durationMillis = 1000)) + // Fade In
                    slideInHorizontally(
                        initialOffsetX = { -40 },
                        animationSpec = tween(durationMillis = 2000)
                    )
        ) {
            TutorialCard(
                text = "Start Tutorial!",
                icon = Icons.Default.PlayArrow,
                onClick = { onIntent(TutorialIntent.ClickedStartTutorial) },
                cardColor = CreamWhite
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        AnimatedVisibility(
            visible = contentVisible,
            modifier = Modifier.weight(1f),
            enter = fadeIn(animationSpec = tween(durationMillis = 1000)) + // Fade In
                    slideInHorizontally(
                        initialOffsetX = { 40 },
                        animationSpec = tween(durationMillis = 2000)
                    )
        ) {
            TutorialCard(
                text = "Skip Tutorial",
                icon = Icons.Default.Done,
                onClick = {
                    onIntent(TutorialIntent.ClickedSkipTutorial)
                },
                cardColor = GreenVariant
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun TutorialScreenPreview() {
    TutorialScreen(
        onNavigateToLogin = {},
        onNavigateContinueTutorial = {}

    )
}