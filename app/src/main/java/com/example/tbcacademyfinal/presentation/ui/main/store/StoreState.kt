package com.example.tbcacademyfinal.presentation.ui.main.store

import androidx.compose.runtime.Immutable
import com.example.tbcacademyfinal.presentation.model.ProductUi

@Immutable
data class StoreState(
    val isLoading: Boolean = true,
    val isSearching: Boolean = false,
    val allProducts: List<ProductUi> = emptyList(),
    val availableCategories: List<String> = emptyList(),
    val currentProducts: List<ProductUi> = emptyList(), //filtristvis
    val minPrice: Float = 0f,
    val maxPrice: Float = 10000f,
    val hasModelOnly: Boolean = false,
    val dailyCollection: List<ProductUi> = emptyList(),
    val dailyItem: ProductUi? = null,
    val isDailyItemInCollection: Boolean = false,
    val error: String? = null,
    val searchQuery: String = "",
    val isNetworkAvailable: Boolean = true,
    val selectedCategory: String? = null,
    val isFiltering: Boolean = false,
)
