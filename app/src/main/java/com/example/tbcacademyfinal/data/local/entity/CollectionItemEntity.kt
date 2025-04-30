package com.example.tbcacademyfinal.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "collection_items")
data class CollectionItemEntity(
    @PrimaryKey val productId: String,
    val name: String,
    val imageUrl: String,
    val modelFile: String,
    val addedAt: Long = System.currentTimeMillis()
)