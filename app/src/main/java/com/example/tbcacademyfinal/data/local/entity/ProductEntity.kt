package com.example.tbcacademyfinal.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.tbcacademyfinal.domain.model.Product

// data/local/ProductEntity.kt
@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val price: Double,
    @ColumnInfo(name = "image_url") val imageUrl: String,
    val category: String,
    @ColumnInfo(name = "model_file") val modelFile: String
)

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
