package com.example.tbcacademyfinal.domain.usecase.collection

import com.example.tbcacademyfinal.domain.model.Product
import com.example.tbcacademyfinal.domain.repository.CollectionRepository
import com.example.tbcacademyfinal.common.Resource
import javax.inject.Inject

class AddToCollectionUseCase @Inject constructor(
    private val collectionRepository: CollectionRepository
) {
    suspend operator fun invoke(product: Product): Resource<Unit> {
        if (product.modelFile.isBlank()) {
            return Resource.Error("Product cannot be added to collection without a model file.")
        }
        return collectionRepository.addItemToCollection(product)
    }
}