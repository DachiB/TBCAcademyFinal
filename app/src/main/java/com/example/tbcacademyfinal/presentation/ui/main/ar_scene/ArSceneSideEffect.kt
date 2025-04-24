package com.example.tbcacademyfinal.presentation.ui.main.ar_scene

sealed interface ArSceneSideEffect {
    data class ShowError(val message: String) : ArSceneSideEffect
    data object NavigateToDetails : ArSceneSideEffect
    // Might need side effects if interacting with other system features
}