package com.example.tbcacademyfinal.di

import android.content.Context
import androidx.room.Room
import com.example.tbcacademyfinal.data.local.AppDatabase
import com.example.tbcacademyfinal.data.local.dao.CollectionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    private const val DATABASE_NAME = "ar_home_designer.db" // Consistent naming

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            DATABASE_NAME
        )
            // Add migrations here if needed in the future:
            // .addMigrations(MIGRATION_1_2)
            .fallbackToDestructiveMigration() // Simple fallback for now during development
            .build()
    }

    @Provides
    @Singleton // DAOs are usually singletons within the DB scope
    fun provideCollectionDao(appDatabase: AppDatabase): CollectionDao {
        return appDatabase.collectionDao()
    }
}