package com.example.tbcacademyfinal.data.remote

import com.example.tbcacademyfinal.data.model.ProductDto
import retrofit2.http.GET

interface ProductApiService {
    @GET("7e4e2812-dd3c-467b-a04e-95b0231f0d6d") // Adjust the endpoint as necessary.
    suspend fun getProducts(): List<ProductDto>
}