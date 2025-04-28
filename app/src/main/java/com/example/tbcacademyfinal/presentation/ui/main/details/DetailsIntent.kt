package com.example.tbcacademyfinal.presentation.ui.main.details

sealed interface DetailsIntent {
    // Note: Loading is triggered automatically by ViewModel init based on nav args
    data object AddToCollectionClicked : DetailsIntent
    data object NavigateBack : DetailsIntent
    data class ClickedModel(val productId: String) : DetailsIntent
}