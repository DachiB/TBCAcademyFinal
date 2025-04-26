package com.example.tbcacademyfinal.data.model

import com.squareup.moshi.Json

data class ProductDto(
    @Json(name = "id") val id: String?,
    @Json(name = "name") val name: String? = "",
    @Json(name = "description") val description: String? = "",
    @Json(name = "price") val price: Double? = 0.0,
    @Json(name = "image_url") val imageUrl: String? = "",
    @Json(name = "category") val category: String? = "",
    @Json(name = "model_file") val modelFile: String? = "" // Asset path for AR model
    // Add other fields as needed from API
)