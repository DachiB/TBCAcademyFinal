package com.example.tbcacademyfinal.presentation.ui.main.collection

sealed interface ArCollectionIntent {
    data object LoadCollection : ArCollectionIntent // Could be triggered by init or refresh
    data class RemoveItem(val productId: String) : ArCollectionIntent
    data object ClearCollection : ArCollectionIntent
    data object StartDecorating : ArCollectionIntent // Trigger AR screen navigation
}