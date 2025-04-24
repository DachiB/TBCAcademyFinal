package com.example.tbcacademyfinal.domain.usecase.collection

import com.example.tbcacademyfinal.domain.model.CollectionItem
import com.example.tbcacademyfinal.domain.repository.CollectionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCollectionItemsUseCase @Inject constructor(
    private val collectionRepository: CollectionRepository
) {
    operator fun invoke(): Flow<List<CollectionItem>> {
        return collectionRepository.getCollectionItems()
    }
}