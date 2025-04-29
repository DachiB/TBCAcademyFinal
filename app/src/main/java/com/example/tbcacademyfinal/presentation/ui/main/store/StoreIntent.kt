package com.example.tbcacademyfinal.presentation.ui.main.store

sealed interface StoreIntent {
    data object LoadProducts : StoreIntent
    data class ProductClicked(val productId: String) : StoreIntent
    data class SearchQueryChanged(val query: String) : StoreIntent
    data object RetryButtonClicked : StoreIntent
    data class CategorySelected(val category: String) : StoreIntent
    data object ClearCategoryFilter : StoreIntent
    data class PriceRangeChanged(val min: Float, val max: Float) : StoreIntent
    data class HasModelOnlyChanged(val hasModel: Boolean) : StoreIntent
    data class FilterButtonClicked(val isFiltering: Boolean) : StoreIntent
    data object ClickedAddDailyCollection : StoreIntent
    data object ClickedAddItemOfDay : StoreIntent
}