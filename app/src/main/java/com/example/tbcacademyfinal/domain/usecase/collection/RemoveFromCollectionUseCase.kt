package com.example.tbcacademyfinal.domain.usecase.collection

import com.example.tbcacademyfinal.domain.repository.CollectionRepository
import com.example.tbcacademyfinal.common.Resource
import javax.inject.Inject

class RemoveFromCollectionUseCase @Inject constructor(
    private val collectionRepository: CollectionRepository
) {
    suspend operator fun invoke(productId: String): Resource<Unit> {
        return collectionRepository.removeItemFromCollection(productId)
    }
}