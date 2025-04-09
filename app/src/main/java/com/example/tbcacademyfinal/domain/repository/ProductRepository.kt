package com.example.tbcacademyfinal.domain.repository

import com.example.tbcacademyfinal.data.model.ProductDto
import com.example.tbcacademyfinal.domain.model.ProductDomain

interface ProductRepository {
    suspend fun getProducts(): List<ProductDomain>
}