package com.example.tbcacademyfinal.di

import com.example.tbcacademyfinal.data.remote.ProductApiService
import com.example.tbcacademyfinal.data.repository.ProductRepositoryRemoteImpl
import com.example.tbcacademyfinal.domain.repository.ProductRepository
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
        productApiService: ProductApiService
    ): ProductRepository {
        return ProductRepositoryRemoteImpl(productApiService)
    }
}