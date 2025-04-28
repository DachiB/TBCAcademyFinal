package com.example.tbcacademyfinal.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tbcacademyfinal.data.local.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    /** For offline-first list */
    @Query("SELECT * FROM products ORDER BY name")
    fun getAllEntities(): Flow<List<ProductEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<ProductEntity>)

    @Query("DELETE FROM products")
    suspend fun clearAll()

    /** Optional: PagingSource if you want to add Paging3 later */
    @Query("SELECT * FROM products ORDER BY name ASC")
    fun pagingSource(): PagingSource<Int, ProductEntity>
}