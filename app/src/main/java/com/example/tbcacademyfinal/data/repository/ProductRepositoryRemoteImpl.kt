package com.example.tbcacademyfinal.data.repository

import com.example.tbcacademyfinal.data.mapper.toDomainModel
import com.example.tbcacademyfinal.data.remote.ProductApiService
import com.example.tbcacademyfinal.domain.model.ProductDomain
import com.example.tbcacademyfinal.domain.repository.ProductRepository

class ProductRepositoryRemoteImpl(
    private val apiService: ProductApiService
) : ProductRepository {
    override suspend fun getProducts(): List<ProductDomain> {
        return apiService.getProducts().map { it.toDomainModel() }
    }
}