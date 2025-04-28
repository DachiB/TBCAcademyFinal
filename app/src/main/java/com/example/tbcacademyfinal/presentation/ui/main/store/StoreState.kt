package com.example.tbcacademyfinal.presentation.ui.main.store

import androidx.compose.runtime.Immutable
import com.example.tbcacademyfinal.presentation.model.ProductUi

// Mark State as immutable for Compose performance
@Immutable
data class StoreState(
    val isLoading: Boolean = true,
    val isSearching: Boolean = false,
    val allProducts: List<ProductUi> = emptyList(),
    val availableCategories: List<String> = emptyList(),
    val currentProducts: List<ProductUi> = emptyList(),
    val error: String? = null,
    val searchQuery: String = "",
    val isNetworkAvailable: Boolean = true,
    val selectedCategory: String? = null
    // Add other state like filters, search query etc. later
)
