package com.example.tbcacademyfinal.presentation.ui.auth.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Email
import androidx.compose.material.icons.twotone.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.tbcacademyfinal.R
import com.example.tbcacademyfinal.common.CollectSideEffect
import kotlinx.coroutines.delay

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onNavigateToMain: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    // Side Effect collection remains the same
    CollectSideEffect(flow = viewModel.event) { effect ->
        when (effect) {
            is LoginSideEffect.NavigateToRegister -> onNavigateToRegister()
            is LoginSideEffect.NavigateToMain -> onNavigateToMain()
            is LoginSideEffect.ShowError -> {
                println("Error: ${effect.message}") // Handle error display
            }
        }
    }

    // Pass the processIntent lambda down
    LoginScreenContent(
        state = viewModel.state, processIntent = viewModel::processIntent // Pass the intent processor
    )
}

@Composable
fun LoginScreenContent(
    state: LoginState,
    // Accept the intent processor function
    processIntent: (LoginIntent) -> Unit
) {
    var contentVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(200) // Small delay before starting animation
        contentVisible = true
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedVisibility(
                visible = contentVisible,
                enter = fadeIn(animationSpec = tween(durationMillis = 800)) +
                        slideInVertically(
                            initialOffsetY = { -40 },
                            animationSpec = tween(durationMillis = 800)
                        )
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            AnimatedVisibility(
                visible = contentVisible,
                enter = fadeIn(animationSpec = tween(durationMillis = 800)) +
                        slideInVertically(
                            initialOffsetY = { -40 },
                            animationSpec = tween(durationMillis = 800)
                        )
            ) {
                Text(
                    text = stringResource(R.string.login_title),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 32.dp)
                )
            }

            AnimatedVisibility(
                visible = contentVisible,
                enter = fadeIn(
                    animationSpec = tween(
                        durationMillis = 800,
                        delayMillis = 200
                    )
                ) + slideInVertically(
                    initialOffsetY = { -40 },
                    animationSpec = tween(durationMillis = 800, delayMillis = 200)
                )
            ) {
                OutlinedTextField(
                    value = state.email,
                    // Send EmailChanged intent on value change
                    onValueChange = { processIntent(LoginIntent.EmailChanged(it)) },
                    label = { Text(stringResource(R.string.email_label)) },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.TwoTone.Email,
                            contentDescription = stringResource(R.string.email_label)
                        )
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    isError = state.errorMessage != null,
                    enabled = !state.isLoading
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            AnimatedVisibility(
                visible = contentVisible,
                enter = fadeIn(
                    animationSpec = tween(
                        durationMillis = 800,
                        delayMillis = 200
                    )
                ) + slideInVertically(
                    initialOffsetY = { -40 },
                    animationSpec = tween(durationMillis = 800, delayMillis = 200)
                )
            ) {
                OutlinedTextField(
                    value = state.password,
                    onValueChange = { processIntent(LoginIntent.PasswordChanged(it)) },
                    label = { Text(stringResource(R.string.password_label)) },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.TwoTone.Lock,
                            contentDescription = stringResource(R.string.password_label)
                        )
                    },
                    singleLine = true,
                    visualTransformation = if (state.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        val image =
                            if (state.isPasswordVisible) R.drawable.visibility_on else R.drawable.visibility_off
                        val description =
                            if (state.isPasswordVisible) stringResource(R.string.hide_password) else stringResource(
                                R.string.show_password
                            )
                        IconButton(onClick = { processIntent(LoginIntent.PasswordVisibilityChanged) }) {
                            Icon(
                                painter = painterResource(id = image),
                                contentDescription = description,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    },
                    isError = state.errorMessage != null,
                    enabled = !state.isLoading
                )
            }

            AnimatedVisibility(
                visible = contentVisible,
                enter = fadeIn(
                    animationSpec = tween(
                        durationMillis = 800,
                        delayMillis = 400
                    )
                ) + slideInVertically(
                    initialOffsetY = { -40 },
                    animationSpec = tween(durationMillis = 800, delayMillis = 400)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp), // Align with error message or spacer
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Checkbox(checked = state.rememberMe, onCheckedChange = { isChecked ->
                        processIntent(
                            LoginIntent.RememberMeChanged(
                                isChecked
                            )
                        )
                    }, enabled = !state.isLoading)
                    // Make the text clickable to toggle the checkbox too
                    Text(
                        text = stringResource(R.string.login_remember_me), // Add string
                        modifier = Modifier.clickable {
                            processIntent(
                                LoginIntent.RememberMeChanged(
                                    !state.rememberMe
                                )
                            )
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            val errorMessage = state.errorMessage // Prioritize validation error
            if (errorMessage != null) {
                Text(
                    text = state.errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(24.dp)) // Maintain space
            } else {
                Spacer(modifier = Modifier.height(24.dp)) // Maintain space
            }


            AnimatedVisibility(
                visible = contentVisible,
                enter = fadeIn(
                    animationSpec = tween(
                        durationMillis = 800,
                        delayMillis = 500
                    )
                ) + slideInVertically(
                    initialOffsetY = { 40 },
                    animationSpec = tween(durationMillis = 800, delayMillis = 500)
                ) // Slide from bottom
            ) {
                Button(
                    // Send LoginClicked intent on click
                    onClick = { processIntent(LoginIntent.LoginClicked) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    enabled = !state.isLoading
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text(stringResource(R.string.login_button))
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Register Link - Dispatch Intent
            AnimatedVisibility(
                visible = contentVisible,
                enter = fadeIn(
                    animationSpec = tween(
                        durationMillis = 800,
                        delayMillis = 600
                    )
                ) + slideInVertically(
                    initialOffsetY = { 40 },
                    animationSpec = tween(durationMillis = 800, delayMillis = 600)
                ) // Slide from bottom
            ) {
                Text(text = stringResource(R.string.register_prompt),
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.primary,
                        textDecoration = TextDecoration.Underline
                    ),

                    modifier = Modifier.clickable {
                        if (!state.isLoading) processIntent(LoginIntent.RegisterLinkClicked)
                    })
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreenContent(state = LoginState(), processIntent = {})
}