package com.example.tbcacademyfinal.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.tbcacademyfinal.data.local.dao.CollectionDao
import com.example.tbcacademyfinal.data.remote.ProductApiService
import com.example.tbcacademyfinal.data.repository.AuthRepositoryImpl
import com.example.tbcacademyfinal.data.repository.CollectionRepositoryImpl
import com.example.tbcacademyfinal.data.repository.DataStoreRepositoryImpl
import com.example.tbcacademyfinal.data.repository.ProductRepositoryImpl
import com.example.tbcacademyfinal.data.repository.StorageRepositoryImpl
import com.example.tbcacademyfinal.data.repository.UserRepositoryImpl
import com.example.tbcacademyfinal.domain.repository.AuthRepository
import com.example.tbcacademyfinal.domain.repository.CollectionRepository
import com.example.tbcacademyfinal.domain.repository.DataStoreRepository
import com.example.tbcacademyfinal.domain.repository.ProductRepository
import com.example.tbcacademyfinal.domain.repository.StorageRepository
import com.example.tbcacademyfinal.domain.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideProductRepository(
        apiService: ProductApiService
    ): ProductRepository {
        return ProductRepositoryImpl(apiService)
    }


    @Provides
    @Singleton
    fun provideAuthRepository(auth: FirebaseAuth): AuthRepository {
        return AuthRepositoryImpl(auth)
    }

    @Provides
    @Singleton
    fun provideUserRepository(firestore: FirebaseFirestore): UserRepository {
        return UserRepositoryImpl(firestore)
    }

    @Provides
    @Singleton
    fun provideCollectionRepository(collectionDao: CollectionDao): CollectionRepository {
        return CollectionRepositoryImpl(collectionDao)
    }

    @Singleton
    @Provides
    fun provideDataStoreRepository(dataStore: DataStore<Preferences>): DataStoreRepository {
        return DataStoreRepositoryImpl(dataStore)
    }

    @Provides
    @Singleton
    fun provideStorageRepository(storage: FirebaseStorage): StorageRepository {
        return StorageRepositoryImpl(storage)
    }

}