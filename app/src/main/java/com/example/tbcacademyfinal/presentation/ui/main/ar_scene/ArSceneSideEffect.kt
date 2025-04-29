package com.example.tbcacademyfinal.presentation.ui.main.ar_scene

sealed interface ArSceneSideEffect {
    data class ShowSnackBar(val message: String) : ArSceneSideEffect
    data object NavigateBack : ArSceneSideEffect

}