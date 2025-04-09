package com.example.tbcacademyfinal.domain.usecase

import com.example.tbcacademyfinal.domain.model.ProductDomain
import com.example.tbcacademyfinal.domain.repository.ProductRepository
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(private val repository: ProductRepository) {
    suspend operator fun invoke(): List<ProductDomain> {
        return repository.getProducts()
    }
}