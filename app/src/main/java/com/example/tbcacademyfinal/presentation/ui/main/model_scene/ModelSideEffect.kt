package com.example.tbcacademyfinal.presentation.ui.main.model_scene

sealed interface ModelSideEffect {
    data class ShowError(val message: String) : ModelSideEffect
    data object NavigateBack : ModelSideEffect
}