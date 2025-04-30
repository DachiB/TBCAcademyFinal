package com.example.tbcacademyfinal.data.repository

import com.example.tbcacademyfinal.common.Resource
import com.example.tbcacademyfinal.common.safecalls.safeDbCall
import com.example.tbcacademyfinal.data.local.dao.CollectionDao
import com.example.tbcacademyfinal.data.mapper.toCollectionEntity
import com.example.tbcacademyfinal.data.mapper.toDomainList
import com.example.tbcacademyfinal.domain.model.CollectionItem
import com.example.tbcacademyfinal.domain.model.Product
import com.example.tbcacademyfinal.domain.repository.CollectionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CollectionRepositoryImpl @Inject constructor(
    private val collectionDao: CollectionDao
) : CollectionRepository {

    override fun getCollectionItems(): Flow<List<CollectionItem>> {
        return collectionDao.getCollectionItems().map { entityList ->
            entityList.toDomainList()
        }
    }

    override fun isItemInCollection(productId: String): Flow<Boolean> {
        return collectionDao.isItemInCollection(productId).map { count ->
            count > 0
        }
    }

    override suspend fun addItemToCollection(product: Product): Resource<Unit> {
        return safeDbCall(
            block = { collectionDao.insertItem(product.toCollectionEntity()) }
        )
    }

    override suspend fun removeItemFromCollection(productId: String): Resource<Unit> {
        return safeDbCall(
            block = { collectionDao.deleteItemById(productId) }
        )
    }

    override suspend fun clearCollection(): Resource<Unit> {
        return safeDbCall(
            block = { collectionDao.clearCollection() }
        )
    }
}