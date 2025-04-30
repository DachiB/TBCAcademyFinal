package com.example.tbcacademyfinal.presentation.ui.main.profile

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tbcacademyfinal.R
import com.example.tbcacademyfinal.common.safecalls.CollectSideEffect
import com.example.tbcacademyfinal.presentation.theme.TBCAcademyFinalTheme
import com.example.tbcacademyfinal.presentation.ui.main.profile.components.LanguageToggleButton
import com.example.tbcacademyfinal.presentation.ui.main.profile.components.PhotoItem
import com.example.tbcacademyfinal.presentation.ui.main.profile.components.ThemeSwitcher


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    onLogout: () -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }

    CollectSideEffect(flow = viewModel.event) { effect ->
        when (effect) {
            is ProfileSideEffect.NavigateToLogin -> onLogout()
            is ProfileSideEffect.ShowMessage -> {
                snackbarHostState.showSnackbar(
                    effect.message,
                    withDismissAction = true
                )
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
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
        Spacer(modifier = Modifier.height(16.dp))
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
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
        } else {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Email: ${state.userEmail ?: "Unknown User"}",
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

            Spacer(modifier = Modifier.height(16.dp))
            when {
                state.isLoadingPhotos -> {
                    CircularProgressIndicator(modifier = Modifier.padding(vertical = 16.dp))
                }

                state.photosError != null -> {
                    Text(
                        text = "Error loading photos: ${state.photosError}",
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                }

                state.photos.isEmpty() -> {
                    Text(
                        text = stringResource(R.string.profile_photos_empty),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                }

                else -> {
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 100.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 300.dp),
                        contentPadding = PaddingValues(vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(state.photos, key = { url -> url }) { photoUrl ->
                            PhotoItem(imageUrl = photoUrl.toString(), onDeleteClick = {
                                processIntent(ProfileIntent.DeletePhotoClicked(photoUrl.toString()))
                            })
                        }
                    }
                    Spacer(modifier = Modifier.weight(1f))


                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Profile Screen Light")
@Composable
fun ProfileScreenPreviewLight() {
    TBCAcademyFinalTheme(darkTheme = false) {
        ProfileScreenContent(
            state = ProfileState(
                userEmail = "test@example.com",
                isLoading = false
            ),
            processIntent = {},
        )
    }
}

@Preview(showBackground = true, name = "Profile Screen Loading")
@Composable
fun ProfileScreenPreviewLoading() {
    TBCAcademyFinalTheme {
        ProfileScreenContent(
            state = ProfileState(isLoading = true),
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
            ),
            processIntent = {},
        )
    }
}
