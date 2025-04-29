package com.example.tbcacademyfinal.data.repository

import com.example.tbcacademyfinal.data.local.dao.CollectionDao
import com.example.tbcacademyfinal.data.mapper.toCollectionEntity
import com.example.tbcacademyfinal.data.mapper.toDomainList
import com.example.tbcacademyfinal.domain.model.CollectionItem
import com.example.tbcacademyfinal.domain.model.Product
import com.example.tbcacademyfinal.domain.repository.CollectionRepository
import com.example.tbcacademyfinal.common.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.IOException // Catch specific DB errors
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
        return withContext(Dispatchers.IO) {
            try {
                collectionDao.insertItem(product.toCollectionEntity())
                Resource.Success(Unit)
            } catch (e: IOException) {
                Resource.Error("Database error: ${e.localizedMessage}", e)
            } catch (e: Exception) {
                Resource.Error("Failed to add item: ${e.localizedMessage}", e)
            }
        }
    }

    override suspend fun removeItemFromCollection(productId: String): Resource<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                collectionDao.deleteItemById(productId)
                Resource.Success(Unit)
            } catch (e: IOException) {
                Resource.Error("Database error: ${e.localizedMessage}", e)
            } catch (e: Exception) {
                Resource.Error("Failed to remove item: ${e.localizedMessage}", e)
            }
        }
    }

    override suspend fun clearCollection(): Resource<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                collectionDao.clearCollection()
                Resource.Success(Unit)
            } catch (e: IOException) {
                Resource.Error("Database error: ${e.localizedMessage}", e)
            } catch (e: Exception) {
                Resource.Error("Failed to clear collection: ${e.localizedMessage}", e)
            }
        }
    }


}