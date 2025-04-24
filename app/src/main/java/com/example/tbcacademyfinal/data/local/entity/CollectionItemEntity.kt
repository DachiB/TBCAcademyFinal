package com.example.tbcacademyfinal.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "collection_items")
data class CollectionItemEntity(
    // Use productId from the Product model as the primary key
    @PrimaryKey val productId: String,
    val name: String,
    val imageUrl: String,
    val modelFile: String, // Crucial: Path to the AR model asset
    val addedAt: Long = System.currentTimeMillis() // Timestamp when added
)