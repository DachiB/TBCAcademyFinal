package com.example.tbcacademyfinal.data.model

import com.squareup.moshi.Json

data class ProductDto(
    val id: Int,
    val name: String,
    @Json(name = "thumbnail_url")
    val thumbnailUrl: String,
    @Json(name = "model_3d_url")
    val model3DUrl: String
)
