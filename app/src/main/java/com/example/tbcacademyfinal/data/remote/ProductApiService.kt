package com.example.tbcacademyfinal.data.remote

import com.example.tbcacademyfinal.data.model.ProductDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductApiService {

    // Example endpoint for getting all products
    @GET("products") // Adjust path based on your mock API or real endpoint
    suspend fun getProducts(): Response<List<ProductDto>> // Return list of DTOs

    // Example endpoint for getting a single product by ID
    @GET("products/{productId}")
    suspend fun getProductById(@Path("productId") id: String): Response<ProductDto>
}