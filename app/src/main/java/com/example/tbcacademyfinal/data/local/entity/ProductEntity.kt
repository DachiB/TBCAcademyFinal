package com.example.tbcacademyfinal.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.tbcacademyfinal.domain.model.Product

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


