package com.example.tbcacademyfinal.presentation.ui.main.details

import androidx.compose.runtime.Immutable
import com.example.tbcacademyfinal.presentation.model.ProductUi

@Immutable
data class DetailsState(
    val isLoading: Boolean = true,
    val product: ProductUi? = null, // Can be null initially or on error
    val error: String? = null,
    val isAddedToCollection: Boolean = false // Example state for button
)