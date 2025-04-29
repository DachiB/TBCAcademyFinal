package com.example.tbcacademyfinal.presentation.ui.auth.register

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.twotone.Email
import androidx.compose.material.icons.twotone.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.tbcacademyfinal.presentation.theme.TBCAcademyFinalTheme
import com.example.tbcacademyfinal.common.CollectSideEffect


@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = hiltViewModel(),
    onRegisterSuccess: () -> Unit,
    onNavigateBackToLogin: () -> Unit
) {

    // Side Effect collection remains the same
    CollectSideEffect(flow = viewModel.event) { effect ->
        when (effect) {
            is RegisterSideEffect.NavigateBackToLogin -> onNavigateBackToLogin()
            is RegisterSideEffect.NavigateToMain -> onRegisterSuccess()
            is RegisterSideEffect.ShowError -> {
                println("Error: ${effect.message}") // Handle error display
            }
        }
    }

    // Pass the processIntent lambda down
    RegisterScreenContent(
        state = viewModel.state,
        processIntent = viewModel::processIntent // Pass intent processor
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreenContent(
    state: RegisterState,
    processIntent: (RegisterIntent) -> Unit
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.back_button)) },
                navigationIcon = {
                    IconButton(onClick = { processIntent(RegisterIntent.NavigateBackClicked) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.navigate_back)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = stringResource(R.string.register_title),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Email Field - Dispatch Intent
            OutlinedTextField(
                value = state.email,
                // Send EmailChanged intent on value change
                onValueChange = { processIntent(RegisterIntent.EmailChanged(it)) },
                label = { Text(stringResource(R.string.email_label)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                isError = state.emailError != null,
                enabled = !state.isLoading,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.TwoTone.Email,
                        contentDescription = stringResource(R.string.email_label)
                    )
                },
                supportingText = {
                    if (state.emailError != null) {
                        Text(
                            text = state.emailError,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = state.password,
                onValueChange = { processIntent(RegisterIntent.PasswordChanged(it)) },
                label = { Text(stringResource(R.string.password_label)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.TwoTone.Lock,
                        contentDescription = stringResource(R.string.password_label)
                    )
                },
                visualTransformation = if (state.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    val image =
                        if (state.isPasswordVisible) R.drawable.visibility_on else R.drawable.visibility_off
                    IconButton(onClick = { processIntent(RegisterIntent.PasswordVisibilityChanged) }) {
                        Icon(
                            painter = painterResource(id = image),
                            contentDescription = if (state.isPasswordVisible) stringResource(R.string.hide_password) else stringResource(
                                R.string.show_password
                            ),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                isError = state.passwordError != null,
                enabled = !state.isLoading,
                supportingText = {
                    if (state.passwordError != null) {
                        Text(
                            text = state.passwordError,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = state.confirmPassword,
                onValueChange = { processIntent(RegisterIntent.ConfirmPasswordChanged(it)) },
                label = { Text(stringResource(R.string.confirm_password_label)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.TwoTone.Lock,
                        contentDescription = stringResource(R.string.password_label)
                    )
                },
                visualTransformation = if (state.isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    val image =
                        if (state.isConfirmPasswordVisible) R.drawable.visibility_on else R.drawable.visibility_off
                    IconButton(onClick = { processIntent(RegisterIntent.ConfirmPasswordVisibilityChanged) }) {
                        Icon(
                            painter = painterResource(id = image),
                            contentDescription = if (state.isConfirmPasswordVisible) stringResource(
                                R.string.hide_password
                            ) else stringResource(
                                R.string.show_password
                            ),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                isError = state.confirmPasswordError != null,
                enabled = !state.isLoading,
                supportingText = {
                    if (state.confirmPasswordError != null) {
                        Text(
                            text = state.confirmPasswordError,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            )

            val errorMessage = state.serverErrorMessage
            if (errorMessage != null) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            } else {
                Spacer(modifier = Modifier.height(24.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { processIntent(RegisterIntent.RegisterClicked) },
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
                    Text(stringResource(R.string.register_button))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(R.string.login_prompt),
                style = TextStyle(
                    color = MaterialTheme.colorScheme.primary,
                    textDecoration = TextDecoration.Underline
                ),
                modifier = Modifier.clickable {
                    if (!state.isLoading)
                        processIntent(RegisterIntent.NavigateBackClicked)
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    TBCAcademyFinalTheme {
        RegisterScreenContent(state = RegisterState(), processIntent = {}) // Pass dummy lambda
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenValidationErrorPreview() {
    TBCAcademyFinalTheme {
        RegisterScreenContent(
            state = RegisterState(),
            processIntent = {})
    }
}