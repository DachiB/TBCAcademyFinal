package com.example.tbcacademyfinal.presentation.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tbcacademyfinal.R
import com.example.tbcacademyfinal.common.safecalls.CollectSideEffect

@Composable
fun SplashScreen(
    viewModel: SplashViewModel = hiltViewModel(),
    onNavigateToMain: () -> Unit,
    onNavigateToAuth: () -> Unit,
    onNavigateToLanding: () -> Unit
) {

    CollectSideEffect(flow = viewModel.uiEvent) { effect ->
        when (effect) {
            is SplashSideEffect.NavigateToMain -> onNavigateToMain()
            is SplashSideEffect.NavigateToAuth -> onNavigateToAuth()
            is SplashSideEffect.NavigateToLanding -> onNavigateToLanding()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background), // Use theme background
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Replace with your actual logo drawable resource
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground), // EXAMPLE LOGO
                contentDescription = stringResource(id = R.string.app_name), // Add string resource
                modifier = Modifier.size(120.dp),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary) // Tint logo with primary color
            )
            Spacer(modifier = Modifier.height(24.dp))
            LinearProgressIndicator(
                modifier = Modifier.width(48.dp),
                color = MaterialTheme.colorScheme.primary // Use primary color for indicator
            )
        }
    }
}