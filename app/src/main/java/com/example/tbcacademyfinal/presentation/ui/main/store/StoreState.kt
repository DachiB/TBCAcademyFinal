package com.example.tbcacademyfinal.presentation.ui.main.store

import androidx.compose.runtime.Immutable
import com.example.tbcacademyfinal.presentation.model.ProductUi

// Mark State as immutable for Compose performance
data class StoreState(
    val isLoading: Boolean = true,
    val isSearching: Boolean = false,
    val products: List<ProductUi> = emptyList(),
    val error: String? = null,
    val searchQuery: String = "",
    val isNetworkAvailable: Boolean = true,
    // Add other state like filters, search query etc. later
)
