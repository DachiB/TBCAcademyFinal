package com.example.tbcacademyfinal.presentation.ui.main.details

sealed interface DetailsSideEffect {
    data object ShowAddedToCollectionMessage : DetailsSideEffect
    data class ShowError(val message: String) : DetailsSideEffect
    data object NavigateBack : DetailsSideEffect
    data class NavigateToModel(val productId: String) : DetailsSideEffect
}