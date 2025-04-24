package com.example.tbcacademyfinal.presentation.ui.main.collection

sealed interface CollectionIntent {
    data object LoadCollection : CollectionIntent // Could be triggered by init or refresh
    data class RemoveItem(val productId: String) : CollectionIntent
    data object ClearCollection : CollectionIntent
    data object StartDecorating : CollectionIntent // Trigger AR screen navigation
}