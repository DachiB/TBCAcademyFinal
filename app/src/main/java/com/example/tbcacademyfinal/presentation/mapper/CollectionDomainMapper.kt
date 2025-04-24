package com.example.tbcacademyfinal.presentation.mapper

import com.example.tbcacademyfinal.domain.model.CollectionItem
import com.example.tbcacademyfinal.presentation.model.CollectionItemUi

fun CollectionItem.toUiModel(): CollectionItemUi {
    return CollectionItemUi(
        productId = productId,
        name = name,
        imageUrl = imageUrl,
        modelFile = modelFile
    )
}

fun List<CollectionItem>.toUiModelList(): List<CollectionItemUi> {
    return map { it.toUiModel() }
}