package com.example.tbcacademyfinal.presentation.ui.main.details

sealed interface DetailsSideEffect {
    data object ShowAddedToCollectionMessage : DetailsSideEffect // e.g., for Snackbar
    data class ShowError(val message: String) : DetailsSideEffect
    data object NavigateBack : DetailsSideEffect // Can also be triggered from VM
    data class NavigateToModel(val productId: String) : DetailsSideEffect
}