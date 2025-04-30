package com.example.tbcacademyfinal.presentation.ui.tutorial

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tbcacademyfinal.R
import com.example.tbcacademyfinal.common.safecalls.CollectSideEffect
import com.example.tbcacademyfinal.presentation.theme.CreamWhite
import com.example.tbcacademyfinal.presentation.theme.GoldAccent
import com.example.tbcacademyfinal.presentation.theme.GreenVariant
import com.example.tbcacademyfinal.presentation.ui.tutorial.components.MainButton

@Composable
fun TutorialScreen(
    viewModel: TutorialViewModel = hiltViewModel(),
    onNavigateToLogin: () -> Unit,
) {
    CollectSideEffect(flow = viewModel.event) { effect ->
        when (effect) {
            is TutorialSideEffect.NavigateToLogin -> onNavigateToLogin.invoke()
        }
    }
    TutorialContent(
        state = viewModel.state,
        onIntent = viewModel::processIntent
    )
}

@Composable
fun TutorialContent(
    state: TutorialState,
    onIntent: (TutorialIntent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),

        ) {
        if (!state.hasClickedNextTutorial) {
            AnimatedVisibility(
                visible = state.contentVisible,
                modifier = Modifier.weight(1f),
                enter = fadeIn(animationSpec = tween(durationMillis = 1000)) + // Fade In
                        slideInHorizontally(
                            initialOffsetX = { -40 },
                            animationSpec = tween(durationMillis = 1200)
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
                visible = state.contentVisible,
                modifier = Modifier.weight(1f),
                enter = fadeIn(animationSpec = tween(durationMillis = 1000)) + // Fade In
                        slideInHorizontally(
                            initialOffsetX = { 40 },
                            animationSpec = tween(durationMillis = 1200)
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
        } else {
            AnimatedVisibility(
                visible = state.contentVisible,
                modifier = Modifier.weight(1f),
                enter = fadeIn(animationSpec = tween(durationMillis = 1000)) + // Fade In
                        slideInHorizontally(
                            initialOffsetX = { -40 },
                            animationSpec = tween(durationMillis = 1200)
                        )
            ) {
                TutorialCard(
                    text = "Add products to Collection",
                    icon = Icons.Default.PlayArrow,
                    onClick = { },
                    cardColor = CreamWhite
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            AnimatedVisibility(
                visible = state.contentVisible,
                modifier = Modifier.weight(1f),
                enter = fadeIn(animationSpec = tween(durationMillis = 1000)) +
                        slideInHorizontally(
                            initialOffsetX = { 40 },
                            animationSpec = tween(durationMillis = 1200)
                        )
            ) {
                TutorialCard(
                    text = "Start Decorating",
                    icon = Icons.Default.Done,
                    onClick = {
                    },
                    cardColor = GreenVariant
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            AnimatedVisibility(
                visible = state.contentVisible,
                modifier = Modifier.weight(1f),
                enter = fadeIn(animationSpec = tween(durationMillis = 1000)) +
                        slideInHorizontally(
                            initialOffsetX = { -40 },
                            animationSpec = tween(durationMillis = 1200)
                        )
            ) {
                TutorialCard(
                    text = "Place AR Models in Real-Time",
                    icon = Icons.Default.Done,
                    onClick = {
                    },
                    cardColor = GoldAccent
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            AnimatedVisibility(
                visible = state.contentVisible,
                enter = fadeIn(animationSpec = tween(durationMillis = 1000)) +
                        slideInVertically(
                            initialOffsetY = { 40 },
                            animationSpec = tween(durationMillis = 1200)
                        )
            ) {
                MainButton(
                    text = stringResource(R.string.finish_tutorial),
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        onIntent(TutorialIntent.ClickedFinishTutorial)
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TutorialScreenPreview() {
    TutorialContent(
        state = TutorialState(),
        onIntent = {}
    )
}