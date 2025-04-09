package com.example.tbcacademyfinal.data.mapper

import com.example.tbcacademyfinal.data.model.ProductDto
import com.example.tbcacademyfinal.domain.model.ProductDomain

fun ProductDto.toDomainModel(): ProductDomain {
    return ProductDomain(
        id = this.id,
        name = this.name,
        thumbnailUrl = this.thumbnailUrl,
        model3DUrl = this.model3DUrl
    )
}