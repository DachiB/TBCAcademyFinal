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

fun Product.toCollectionEntity(): CollectionItemEntity {
    return CollectionItemEntity(
        productId = this.id,
        name = this.name,
        imageUrl = this.imageUrl,
        modelFile = this.modelFile
    )
}