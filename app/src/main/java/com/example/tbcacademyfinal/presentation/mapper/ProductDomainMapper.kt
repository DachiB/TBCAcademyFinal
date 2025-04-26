package com.example.tbcacademyfinal.presentation.mapper

import com.example.tbcacademyfinal.domain.model.Product
import com.example.tbcacademyfinal.presentation.model.ProductUi

fun Product.toUiModel(): ProductUi {
    // Example price formatting
    val formattedPrice = "$price $"

    return ProductUi(
        id = id,
        name = name,
        description = description,
        formattedPrice = formattedPrice,
        imageUrl = imageUrl,
        category = category,
        modelFile = modelFile
    )
}

// Optional: Mapper for a list
fun List<Product>.toUiModelList(): List<ProductUi> {
    return map { it.toUiModel() }
}