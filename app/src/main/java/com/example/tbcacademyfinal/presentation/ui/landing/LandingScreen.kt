package com.example.tbcacademyfinal.presentation.ui.landing

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tbcacademyfinal.R
import com.example.tbcacademyfinal.presentation.theme.TBCAcademyFinalTheme
import kotlinx.coroutines.delay

@Composable
fun LandingScreen(
    viewModel: LandingViewModel = hiltViewModel(),
    onNavigateToTutorial: () -> Unit
) {
    LandingScreenContent(
        onProceedClicked = {
            viewModel.onProceed() // Mark landing as seen
            onNavigateToTutorial() // Trigger navigation via the lambda
        }
    )
}

// Opt-in for AnimatedVisibility
@Composable
fun LandingScreenContent(
    onProceedClicked: () -> Unit
) {
    // State to control the visibility of animated content
    var contentVisible by remember { mutableStateOf(false) }

    // Trigger the animation shortly after the composable enters the composition
    LaunchedEffect(Unit) {
        delay(500) // Small delay before starting animation
        contentVisible = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween // Use SpaceBetween for alignment
    ) {
        // Animated content grouping (Top part)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f) // Take available space
                .padding(top = 32.dp),
            verticalArrangement = Arrangement.Center // Center content vertically within this Column
        ) {

            // App Logo / Graphic with animation
            AnimatedVisibility(
                visible = contentVisible,
                enter = fadeIn(animationSpec = tween(durationMillis = 1000)) + // Fade In
                        slideInVertically(initialOffsetY = { -40 }, animationSpec = tween(durationMillis = 1000)), // Slide In from Top
                modifier = Modifier.padding(bottom = 24.dp) // Apply padding here if needed
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground), // Replace if needed
                    contentDescription = stringResource(R.string.landing_image_description),
                    modifier = Modifier.size(150.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.height(16.dp)) // Maintain spacing


            // App Title with animation (delayed)
            AnimatedVisibility(
                visible = contentVisible,
                enter = fadeIn(animationSpec = tween(durationMillis = 1000, delayMillis = 200)) + // Delayed Fade
                        slideInVertically(initialOffsetY = { -40 }, animationSpec = tween(durationMillis = 1000, delayMillis = 200)) // Delayed Slide
            ) {
                Text(
                    text = stringResource(id = R.string.app_name),
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // App Description with animation (further delayed)
            AnimatedVisibility(
                visible = contentVisible,
                enter = fadeIn(animationSpec = tween(durationMillis = 1000, delayMillis = 400)) + // More Delayed Fade
                        slideInVertically(initialOffsetY = { -40 }, animationSpec = tween(durationMillis = 1000, delayMillis = 400)), // More Delayed Slide
                modifier = Modifier.padding(horizontal = 16.dp) // Apply padding here
            ) {
                Text(
                    text = stringResource(R.string.landing_description),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                )
            }
        } // End of animated content Column


        // Proceed Button - can also be animated or appear normally
        AnimatedVisibility(
            visible = contentVisible,
            enter = fadeIn(animationSpec = tween(durationMillis = 1000, delayMillis = 600)) + // Even more Delayed Fade
                    slideInVertically(initialOffsetY = { 40 }, animationSpec = tween(durationMillis = 1000, delayMillis = 600)), // Slide In from Bottom
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp) // Padding applied here
                .padding(bottom = 48.dp) // Ensure sufficient bottom padding
        ) {
            Button(
                onClick = onProceedClicked,
                modifier = Modifier
                    .fillMaxWidth() // Button fills the width provided by modifier above
                    .height(50.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = stringResource(R.string.landing_get_started),
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun LandingScreenPreview() {
    TBCAcademyFinalTheme {
        LandingScreenContent(onProceedClicked = {})
    }
}
