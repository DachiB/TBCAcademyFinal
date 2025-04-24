package com.example.tbcacademyfinal.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.tbcacademyfinal.data.local.dao.CollectionDao
import com.example.tbcacademyfinal.data.local.entity.CollectionItemEntity

@Database(entities = [CollectionItemEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun collectionDao(): CollectionDao
    // Companion object usually used for Singleton pattern if NOT using Hilt
    // companion object {
    //     const val DATABASE_NAME = "ar_home_designer_db"
    // }
}