package com.example.tbcacademyfinal.di

import com.example.tbcacademyfinal.data.remote.ProductApiService
import com.example.tbcacademyfinal.data.repository.AuthRepositoryImpl
import com.example.tbcacademyfinal.data.repository.ProductRepositoryImpl
import com.example.tbcacademyfinal.data.repository.UserRepositoryImpl
import com.example.tbcacademyfinal.domain.repository.AuthRepository
import com.example.tbcacademyfinal.domain.repository.ProductRepository
import com.example.tbcacademyfinal.domain.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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
    @Singleton // Or @ViewModelScoped
    fun provideAuthRepository(auth: FirebaseAuth): AuthRepository {
        return AuthRepositoryImpl(auth)
    }

    @Provides
    @Singleton
    fun provideUserRepository(firestore: FirebaseFirestore): UserRepository {
        return UserRepositoryImpl(firestore)
    }
}