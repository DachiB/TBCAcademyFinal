package com.example.tbcacademyfinal.presentation.ui.auth.login


sealed interface LoginIntent {
    data class EmailChanged(val email: String) : LoginIntent
    data class PasswordChanged(val password: String) : LoginIntent
    data object PasswordVisibilityChanged : LoginIntent
    data class RememberMeChanged(val isChecked: Boolean) : LoginIntent
    data object LoginClicked : LoginIntent
    data object RegisterLinkClicked : LoginIntent
}