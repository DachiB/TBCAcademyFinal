package com.example.tbcacademyfinal.domain.repository

import com.example.tbcacademyfinal.domain.model.CollectionItem
import com.example.tbcacademyfinal.domain.model.Product
import com.example.tbcacademyfinal.common.Resource
import kotlinx.coroutines.flow.Flow

interface CollectionRepository {
    fun getCollectionItems(): Flow<List<CollectionItem>>
    fun isItemInCollection(productId: String): Flow<Boolean>
    suspend fun addItemToCollection(product: Product): Resource<Unit>
    suspend fun removeItemFromCollection(productId: String): Resource<Unit>
    suspend fun clearCollection(): Resource<Unit>
}