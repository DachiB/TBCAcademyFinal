package com.example.tbcacademyfinal.presentation.ui.main.store

sealed interface StoreIntent {
    data object LoadProducts : StoreIntent
    data class ProductClicked(val productId: String) : StoreIntent
    // Add intents for filter changes, search, etc. later
}