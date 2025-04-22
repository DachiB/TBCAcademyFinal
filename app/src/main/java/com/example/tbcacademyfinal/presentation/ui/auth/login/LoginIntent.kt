package com.example.tbcacademyfinal.presentation.ui.auth.login

// State and SideEffect remain the same

// --- Add LoginIntent ---
sealed interface LoginIntent {
    data class EmailChanged(val email: String) : LoginIntent
    data class PasswordChanged(val password: String) : LoginIntent
    data object PasswordVisibilityChanged : LoginIntent
    data object LoginClicked : LoginIntent
    data object RegisterLinkClicked : LoginIntent // Renamed for clarity
}