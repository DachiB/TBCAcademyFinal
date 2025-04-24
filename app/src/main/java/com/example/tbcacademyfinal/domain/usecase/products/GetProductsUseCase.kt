package com.example.tbcacademyfinal.domain.usecase.products

import com.example.tbcacademyfinal.domain.model.Product
import com.example.tbcacademyfinal.domain.repository.ProductRepository
import com.example.tbcacademyfinal.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    operator fun invoke(): Flow<Resource<List<Product>>> {
        return productRepository.getProducts()
    }
}