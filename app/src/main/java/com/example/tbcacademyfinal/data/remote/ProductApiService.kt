package com.example.tbcacademyfinal.data.remote

import com.example.tbcacademyfinal.data.model.ProductDto
import retrofit2.http.GET

interface ProductApiService {
    @GET("cdf9be25-4a4c-4d30-98ab-ac46b74496d0") // Adjust the endpoint as necessary.
    suspend fun getProducts(): List<ProductDto>
}