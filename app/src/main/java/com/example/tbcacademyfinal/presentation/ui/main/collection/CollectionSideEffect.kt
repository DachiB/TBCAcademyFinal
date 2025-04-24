package com.example.tbcacademyfinal.presentation.ui.main.collection

sealed interface CollectionSideEffect {
    data class ShowError(val message: String) : CollectionSideEffect
    data object NavigateToArScreen : CollectionSideEffect // Navigate to the actual AR view
}