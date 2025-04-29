package com.example.tbcacademyfinal.presentation.mapper

import com.example.tbcacademyfinal.domain.model.Product
import com.example.tbcacademyfinal.presentation.model.ProductUi

fun ProductUi.toDomainModel(): Product {
    val price = formattedPrice.removePrefix("$").toDouble()
    return Product(
        id = id,
        name = name,
        description = description,
        price = price,
        imageUrl = imageUrl,
        category = category,
        modelFile = modelFile
    )
}

fun Product.toUiModel(): ProductUi {
    val formattedPrice = "$$price"

    return ProductUi(
        id = id,
        name = name,
        description = description,
        rawPrice = price,
        formattedPrice = formattedPrice,
        imageUrl = imageUrl,
        category = category,
        modelFile = modelFile
    )
}

fun List<Product>.toUiModelList(): List<ProductUi> {
    return map { it.toUiModel() }
}