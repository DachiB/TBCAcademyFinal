package com.example.tbcacademyfinal.data.mapper

import com.example.tbcacademyfinal.data.local.entity.ProductEntity
import com.example.tbcacademyfinal.data.model.ProductDto
import com.example.tbcacademyfinal.domain.model.Product

fun ProductDto.toDomain(): Product? {
    return Product(
        id = id ?: return null,
        name = name ?: "Unknown Product",
        description = description ?: "No description available.",
        price = price ?: 0.0,
        imageUrl = imageUrl ?: "",
        category = category ?: "Uncategorized",
        modelFile = modelFile ?: ""
    )
}

fun Product.toEntity() = ProductEntity(
    id = id,
    name = name,
    description = description,
    price = price,
    imageUrl = imageUrl,
    category = category,
    modelFile = modelFile
)

fun ProductEntity.toDomain() = Product(
    id = id,
    name = name, description = description,
    price = price,
    imageUrl = imageUrl,
    category = category,
    modelFile = modelFile
)

fun List<ProductDto>.toDomainList(): List<Product> {
    return mapNotNull { it.toDomain() }
}