package com.example.tbcacademyfinal.presentation.ui.main.collection

import com.example.tbcacademyfinal.presentation.model.CollectionItemUi

sealed interface CollectionIntent {
    data object LoadCollection : CollectionIntent
    data class RemoveItem(val productId: String) : CollectionIntent
    data object ClearCollection : CollectionIntent
    data class BuyCollection(val collection: List<CollectionItemUi>?) : CollectionIntent
    data object StartDecorating : CollectionIntent
}