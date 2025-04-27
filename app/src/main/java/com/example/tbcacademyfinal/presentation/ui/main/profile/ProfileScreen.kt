package com.example.tbcacademyfinal.presentation.ui.main.profile

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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.tbcacademyfinal.R
import com.example.tbcacademyfinal.common.CollectSideEffect
import com.example.tbcacademyfinal.presentation.theme.TBCAcademyFinalTheme
import com.example.tbcacademyfinal.presentation.ui.main.profile.settings.LanguageToggleButton
import com.example.tbcacademyfinal.presentation.ui.main.profile.settings.SettingsViewModel
import com.example.tbcacademyfinal.presentation.ui.main.profile.settings.ThemeSwitcher


@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    settingViewModel: SettingsViewModel = hiltViewModel(),
    onLogout: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    CollectSideEffect(flow = viewModel.event) { effect ->
        when (effect) {
            is ProfileSideEffect.NavigateToLogin -> onLogout()
        }
    }


    ProfileScreenContent(
        state = state,
        processIntent = viewModel::processIntent,
        settingsViewModel = settingViewModel
    )
}


@Composable
fun ProfileScreenContent(
    state: ProfileState,
    settingsViewModel: SettingsViewModel,
    processIntent: (ProfileIntent) -> Unit
) {
    val isDarkTheme by settingsViewModel.darkThemeFlow.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.profile_title), // Keep title
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .padding(bottom = 24.dp)
                    .align(Alignment.CenterVertically),
                textAlign = TextAlign.Start
            )
            ThemeSwitcher(
                size = 50.dp,
                padding = 4.dp,
                darkTheme = isDarkTheme,
                onClick = { settingsViewModel.toggleTheme(isDarkTheme) }
            )

        }
        LanguageToggleButton(settingsViewModel)

        if (state.isLoading) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.weight(1f)) // Push logout to bottom
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
            Text(
                // Use the updated string resource if desired, or just display email
                text = "Email: ${state.userEmail}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // --- Theme/Language UI Removed ---

            Spacer(modifier = Modifier.weight(1f)) // Push logout button to bottom

            // Logout Button (remains the same)
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
            settingsViewModel = hiltViewModel()
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
            settingsViewModel = hiltViewModel()
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
            settingsViewModel = hiltViewModel()
        )
    }
}
