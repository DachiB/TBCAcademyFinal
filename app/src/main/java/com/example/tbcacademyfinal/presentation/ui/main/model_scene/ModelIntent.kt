package com.example.tbcacademyfinal.presentation.ui.main.model_scene

sealed interface ModelIntent {
    data object ClickedBack : ModelIntent
}