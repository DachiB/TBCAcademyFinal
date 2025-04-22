package com.example.tbcacademyfinal.presentation.ui.auth.register

sealed interface RegisterIntent {
    data class EmailChanged(val email: String) : RegisterIntent
    data class PasswordChanged(val password: String) : RegisterIntent
    data object PasswordVisibilityChanged : RegisterIntent
    data class ConfirmPasswordChanged(val confirmPassword: String) : RegisterIntent
    data object ConfirmPasswordVisibilityChanged : RegisterIntent
    data object RegisterClicked : RegisterIntent
    data object NavigateBackClicked : RegisterIntent // Renamed for clarity
}
