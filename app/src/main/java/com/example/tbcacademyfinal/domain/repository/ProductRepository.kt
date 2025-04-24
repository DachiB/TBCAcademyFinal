package com.example.tbcacademyfinal.domain.repository

import com.example.tbcacademyfinal.domain.model.Product
import com.example.tbcacademyfinal.util.Resource
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getProducts(): Flow<Resource<List<Product>>>
    fun getProductById(id: String): Flow<Resource<Product>>
}