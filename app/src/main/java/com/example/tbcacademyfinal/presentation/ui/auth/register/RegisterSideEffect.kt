package com.example.tbcacademyfinal.presentation.ui.auth.register

sealed interface RegisterSideEffect {
    data class ShowError(val message: String) : RegisterSideEffect
    data object NavigateBackToLogin : RegisterSideEffect
    data object NavigateToMain : RegisterSideEffect
}