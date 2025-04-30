package com.example.tbcacademyfinal.presentation.ui.main.details

sealed interface DetailsIntent {
    data object AddToCollectionClicked : DetailsIntent
    data object NavigateBack : DetailsIntent
    data class ClickedModel(val productId: String) : DetailsIntent
}