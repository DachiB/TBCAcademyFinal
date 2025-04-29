package com.example.tbcacademyfinal.presentation.ui.landing

sealed interface LandingSideEffect {
    data object NavigateToTutorial : LandingSideEffect
}