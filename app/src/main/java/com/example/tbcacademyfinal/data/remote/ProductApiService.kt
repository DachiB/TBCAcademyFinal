package com.example.tbcacademyfinal.data.remote

import com.example.tbcacademyfinal.data.model.ProductDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductApiService {
    @GET("products")
    suspend fun getProducts(): Response<List<ProductDto>>

    @GET("products/{productId}")
    suspend fun getProductById(@Path("productId") id: String): Response<ProductDto>
}