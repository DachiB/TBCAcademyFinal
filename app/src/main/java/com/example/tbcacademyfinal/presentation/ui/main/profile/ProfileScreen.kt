package com.example.tbcacademyfinal.presentation.ui.main.profile

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tbcacademyfinal.R
import com.example.tbcacademyfinal.common.CollectSideEffect
import com.example.tbcacademyfinal.presentation.theme.TBCAcademyFinalTheme
import com.example.tbcacademyfinal.presentation.ui.main.profile.settings.LanguageToggleButton
import com.example.tbcacademyfinal.presentation.ui.main.profile.settings.ThemeSwitcher


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    onLogout: () -> Unit,
) {

    CollectSideEffect(flow = viewModel.event) { effect ->
        when (effect) {
            is ProfileSideEffect.NavigateToLogin -> onLogout()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.profile_title))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        ProfileScreenContent(
            modifier = Modifier.padding(paddingValues),
            state = viewModel.state,
            processIntent = viewModel::processIntent,
        )
    }
}


@Composable
fun ProfileScreenContent(
    modifier: Modifier = Modifier,
    state: ProfileState,
    processIntent: (ProfileIntent) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {

            LanguageToggleButton(
                modifier = Modifier.align(Alignment.CenterVertically),
                state, processIntent
            )
            ThemeSwitcher(
                size = 50.dp,
                padding = 4.dp,
                darkTheme = state.isDarkTheme,
                onClick = { processIntent(ProfileIntent.ThemeChanged(state.isDarkTheme)) }
            )

        }

        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 16.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
        } else if (state.error != null) {
            Log.d("ProfileScreen", "Error: ${state.error}")
            Text(
                text = "Error: ${state.error}",
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp) // Add padding below error
            )
            Spacer(modifier = Modifier.weight(1f)) // Push logout to bottom
        } else {
            // Display User Email (Main Content)

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "Email: ${state.userEmail}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Button(
                onClick = { processIntent(ProfileIntent.LogoutClicked) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = null,
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text(stringResource(R.string.profile_logout_button))
            }
        }
    }
}


// --- Previews (Update to reflect simplified state) ---
@Preview(showBackground = true, name = "Profile Screen Light")
@Composable
fun ProfileScreenPreviewLight() {
    TBCAcademyFinalTheme(darkTheme = false) {
        ProfileScreenContent(
            state = ProfileState(
                userEmail = "test@example.com",
                isLoading = false
            ), // Simplified state
            processIntent = {},
        )
    }
}

@Preview(showBackground = true, name = "Profile Screen Loading")
@Composable
fun ProfileScreenPreviewLoading() {
    TBCAcademyFinalTheme {
        ProfileScreenContent(
            state = ProfileState(isLoading = true), // Simplified state
            processIntent = {},
        )
    }
}

@Preview(showBackground = true, name = "Profile Screen Error")
@Composable
fun ProfileScreenPreviewError() {
    TBCAcademyFinalTheme {
        ProfileScreenContent(
            state = ProfileState(
                isLoading = false,
                error = "Failed to load profile"
            ), // Simplified state
            processIntent = {},
        )
    }
}
