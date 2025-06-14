package com.example.tbcacademyfinal.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.tbcacademyfinal.data.local.dao.CollectionDao
import com.example.tbcacademyfinal.data.local.entity.CollectionItemEntity

@Database(entities = [CollectionItemEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun collectionDao(): CollectionDao
}