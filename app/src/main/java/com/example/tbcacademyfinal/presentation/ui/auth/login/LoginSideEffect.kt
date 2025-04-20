package com.example.tbcacademyfinal.presentation.ui.auth.login

sealed interface LoginSideEffect {
    data class ShowError(val message: String) : LoginSideEffect
    data object NavigateToRegister : LoginSideEffect
    data object NavigateToMain : LoginSideEffect // Alternatively handle via state change
}