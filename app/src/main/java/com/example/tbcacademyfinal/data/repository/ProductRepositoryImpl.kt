package com.example.tbcacademyfinal.data.repository

import com.example.tbcacademyfinal.data.mapper.toDomain
import com.example.tbcacademyfinal.data.mapper.toDomainList
import com.example.tbcacademyfinal.data.model.ProductDto
import com.example.tbcacademyfinal.data.remote.ProductApiService
import com.example.tbcacademyfinal.domain.model.Product
import com.example.tbcacademyfinal.domain.repository.ProductRepository
import com.example.tbcacademyfinal.util.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton // Make repository singleton
class ProductRepositoryImpl @Inject constructor(
    // Inject ApiService even for fake implementation to prepare for switch
    private val apiService: ProductApiService
) : ProductRepository {

    // --- FAKE DATA ---
    // Replace this with actual API call using apiService later
    private val fakeProductDtos = listOf(
        ProductDto(
            "p1",
            "Modern Sofa",
            "A comfy modern sofa.",
            1299.99,
            "https://atlas-content-cdn.pixelsquid.com/stock-images/sofa-single-ywYZBl8-600.jpg",
            "Sofas",
            "models/sofa.glb"
        ),
        ProductDto(
            "p2",
            "Minimalist Lamp",
            "Sleek floor lamp.",
            149.50,
            "https://www.ikea.com/us/en/images/products/arstid-floor-lamp-with-led-bulb-brass-white__0390610_pe566328_s5.jpg",
            "Lighting",
            "models/lamp.glb"
        ),
        ProductDto(
            "p3",
            "Oak Coffee Table",
            "Solid wood coffee table.",
            399.00,
            "https://example.com/images/table.jpg",
            "Tables",
            "models/table.glb"
        ),
        ProductDto(
            "p4",
            "Abstract Wall Art",
            "Canvas print for your wall.",
            89.00,
            "https://example.com/images/art.jpg",
            "Decor",
            "models/art.glb"
        ), // Assuming decor might not have 3D models
        ProductDto(
            "p5",
            "Ergonomic Chair",
            "Office chair for comfort.",
            250.00,
            "https://example.com/images/chair.jpg",
            "Chairs",
            "models/chair.glb"
        ),
        ProductDto(
            "p6",
            "Velvet Armchair",
            "Plush green armchair.",
            450.00,
            "https://example.com/images/armchair.jpg",
            "Chairs",
            "models/armchair.glb"
        )
    )
    // --- END FAKE DATA ---

    override fun getProducts(): Flow<Resource<List<Product>>> = flow {
        emit(Resource.Loading)
        delay(1000) // Simulate network delay
        try {
            // Fake logic: Directly use the fake DTO list and map it
            val domainProducts = fakeProductDtos.toDomainList()
            emit(Resource.Success(domainProducts))

            // --- LATER: Replace with actual API call ---
            // val response = apiService.getProducts()
            // if (response.isSuccessful) {
            //     val dtos = response.body()
            //     if (dtos != null) {
            //         emit(Resource.Success(dtos.toDomainList()))
            //     } else {
            //         emit(Resource.Error("Empty response body"))
            //     }
            // } else {
            //     emit(Resource.Error("API Error: ${response.code()} ${response.message()}"))
            // }
            // --- End Real API Call ---

        } catch (e: Exception) {
            // Catch potential mapping errors or other exceptions
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }

    override fun getProductById(id: String): Flow<Resource<Product>> = flow {
        emit(Resource.Loading)
        delay(500) // Simulate network delay
        try {
            // Fake logic: Find the DTO in the fake list and map it
            val productDto = fakeProductDtos.find { it.id == id }
            if (productDto != null) {
                val domainProduct = productDto.toDomain()
                if (domainProduct != null) {
                    emit(Resource.Success(domainProduct))
                } else {
                    emit(Resource.Error("Failed to map product DTO for ID: $id"))
                }
            } else {
                emit(Resource.Error("Product with ID $id not found."))
            }

            // --- LATER: Replace with actual API call ---
            // val response = apiService.getProductById(id)
            // if (response.isSuccessful) {
            //     val dto = response.body()
            //     if (dto != null) {
            //          val domainProduct = dto.toDomain()
            //          if (domainProduct != null) {
            //              emit(Resource.Success(domainProduct))
            //          } else {
            //              emit(Resource.Error("Failed to map product DTO for ID: $id"))
            //          }
            //     } else {
            //         emit(Resource.Error("Empty response body for ID: $id"))
            //     }
            // } else {
            //     emit(Resource.Error("API Error for ID $id: ${response.code()} ${response.message()}"))
            // }
            // --- End Real API Call ---

        } catch (e: Exception) {
            emit(
                Resource.Error(
                    e.localizedMessage ?: "An unexpected error occurred fetching product $id"
                )
            )
        }
    }
}