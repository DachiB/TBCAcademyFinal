package com.example.tbcacademyfinal.domain.usecase.collection

import com.example.tbcacademyfinal.domain.repository.CollectionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class IsItemInCollectionUseCase @Inject constructor(
    private val collectionRepository: CollectionRepository
) {
    operator fun invoke(productId: String): Flow<Boolean> {
        return collectionRepository.isItemInCollection(productId)
    }
}