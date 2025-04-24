package com.example.tbcacademyfinal.domain.repository

import com.example.tbcacademyfinal.domain.model.CollectionItem
import com.example.tbcacademyfinal.domain.model.Product
import com.example.tbcacademyfinal.util.Resource
import kotlinx.coroutines.flow.Flow

interface CollectionRepository {
    fun getCollectionItems(): Flow<List<CollectionItem>>
    fun isItemInCollection(productId: String): Flow<Boolean> // Simplified observable check
    suspend fun addItemToCollection(product: Product): Resource<Unit> // Use Resource for potential errors
    suspend fun removeItemFromCollection(productId: String): Resource<Unit>
    suspend fun clearCollection(): Resource<Unit>
}