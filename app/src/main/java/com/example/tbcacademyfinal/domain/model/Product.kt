package com.example.tbcacademyfinal.domain.model

data class Product(
    val id: String, // Non-nullable in domain if required
    val name: String,
    val description: String,
    val price: Double,
    val imageUrl: String,
    val category: String,
    val modelFile: String // Path to the 3D model in assets
    // Add other core business fields
)