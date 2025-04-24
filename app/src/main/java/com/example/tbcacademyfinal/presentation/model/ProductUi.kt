package com.example.tbcacademyfinal.presentation.model

import androidx.compose.runtime.Immutable

@Immutable
data class ProductUi(
    val id: String,
    val name: String,
    val description: String,
    val formattedPrice: String, // Example: Format price as "$XX.XX"
    val imageUrl: String,
    val category: String,
    val modelFile: String
    // Add UI-specific fields or formatted data
)