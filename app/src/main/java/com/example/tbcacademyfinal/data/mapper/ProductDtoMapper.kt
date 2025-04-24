package com.example.tbcacademyfinal.data.mapper

import com.example.tbcacademyfinal.data.model.ProductDto
import com.example.tbcacademyfinal.domain.model.Product

// Function to map DTO to Domain model
// Perform null checks and provide defaults or throw errors as appropriate
fun ProductDto.toDomain(): Product? { // Return nullable if DTO can be invalid
    return Product(
        id = id ?: return null, // Return null if essential ID is missing
        name = name ?: "Unknown Product", // Provide default if name is missing
        description = description ?: "No description available.",
        price = price ?: 0.0,
        imageUrl = imageUrl ?: "", // Provide empty string or placeholder URL
        category = category ?: "Uncategorized",
        modelFile = modelFile ?: "" // Ensure model file path is handled
    )
}

// Optional: Mapper for a list
fun List<ProductDto>.toDomainList(): List<Product> {
    return mapNotNull { it.toDomain() } // Use mapNotNull to filter out invalid DTOs
}