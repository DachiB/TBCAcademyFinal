package com.example.tbcacademyfinal.data.mapper

import com.example.tbcacademyfinal.data.local.entity.CollectionItemEntity
import com.example.tbcacademyfinal.domain.model.CollectionItem
import com.example.tbcacademyfinal.domain.model.Product

fun CollectionItemEntity.toDomain(): CollectionItem {
    return CollectionItem(
        productId = productId,
        name = name,
        imageUrl = imageUrl,
        modelFile = modelFile
    )
}

fun List<CollectionItemEntity>.toDomainList(): List<CollectionItem> {
    return map { it.toDomain() }
}

// Optional: Map Domain Product directly to Entity for adding
fun Product.toCollectionEntity(): CollectionItemEntity {
    return CollectionItemEntity(
        productId = this.id,
        name = this.name,
        imageUrl = this.imageUrl,
        modelFile = this.modelFile
        // addedAt will use default value on insert
    )
}