package com.example.tbcacademyfinal.presentation.model

import androidx.compose.runtime.Immutable

@Immutable
data class ProductUi(
    val id: String,
    val name: String,
    val description: String,
    val rawPrice: Double,
    val formattedPrice: String,
    val imageUrl: String,
    val category: String,
    val modelFile: String
)