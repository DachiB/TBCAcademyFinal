package com.example.tbcacademyfinal.presentation.model

import androidx.compose.runtime.Immutable

@Immutable
data class CollectionItemUi(
    val productId: String,
    val name: String,
    val imageUrl: String,
    val modelFile: String // Needed for AR launch
)