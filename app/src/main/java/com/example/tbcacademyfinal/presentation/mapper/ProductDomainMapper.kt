package com.example.tbcacademyfinal.presentation.mapper

import com.example.tbcacademyfinal.domain.model.Product
import com.example.tbcacademyfinal.presentation.model.ProductUi
import java.text.NumberFormat
import java.util.Locale

fun Product.toUiModel(): ProductUi {
    // Example price formatting
    val format =
        NumberFormat.getCurrencyInstance(Locale.getDefault()) // Use appropriate Locale/Currency
    val formattedPrice = try {
        format.format(price)
    } catch (e: IllegalArgumentException) {
        "$price (Format Error)" // Fallback if formatting fails
    }

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