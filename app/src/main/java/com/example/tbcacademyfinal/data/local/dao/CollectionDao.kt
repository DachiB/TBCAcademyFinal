package com.example.tbcacademyfinal.data.local.dao

import androidx.room.*
import com.example.tbcacademyfinal.data.local.entity.CollectionItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CollectionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: CollectionItemEntity)

    @Query("SELECT * FROM collection_items ORDER BY addedAt DESC")
    fun getCollectionItems(): Flow<List<CollectionItemEntity>>

    @Query("SELECT COUNT(*) FROM collection_items WHERE productId = :productId")
    fun isItemInCollection(productId: String): Flow<Int>

    @Query("SELECT * FROM collection_items WHERE productId = :productId LIMIT 1")
    suspend fun getItemById(productId: String): CollectionItemEntity?

    @Delete
    suspend fun deleteItem(item: CollectionItemEntity)

    @Query("DELETE FROM collection_items WHERE productId = :productId")
    suspend fun deleteItemById(productId: String)

    @Query("DELETE FROM collection_items")
    suspend fun clearCollection()

}