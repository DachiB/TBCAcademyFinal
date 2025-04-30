package com.example.tbcacademyfinal.presentation.ui.main.profile

sealed interface ProfileSideEffect {
    data object NavigateToLogin : ProfileSideEffect
    data class ShowMessage(val message: String) : ProfileSideEffect
}