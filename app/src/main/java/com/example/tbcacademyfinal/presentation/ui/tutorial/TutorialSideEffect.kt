package com.example.tbcacademyfinal.presentation.ui.tutorial

sealed interface TutorialSideEffect {
    data object NavigateToLogin : TutorialSideEffect
}