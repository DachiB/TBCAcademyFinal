package com.example.tbcacademyfinal.data.repository

import com.example.tbcacademyfinal.data.mapper.toDomain
import com.example.tbcacademyfinal.data.mapper.toDomainList
import com.example.tbcacademyfinal.data.remote.ProductApiService
import com.example.tbcacademyfinal.domain.model.Product
import com.example.tbcacademyfinal.domain.repository.ProductRepository
import com.example.tbcacademyfinal.common.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton // Make repository singleton
class ProductRepositoryImpl @Inject constructor(
    // Inject ApiService even for fake implementation to prepare for switch
    private val apiService: ProductApiService
) : ProductRepository {


    override fun getProducts(): Flow<Resource<List<Product>>> = flow {
        emit(Resource.Loading)
        try {
            // --- Actual API Call ---
            val response = apiService.getProducts() // Call the suspend function
            if (response.isSuccessful) {
                val productDtoList = response.body()
                if (productDtoList != null) {
                    // Map DTO list to Domain list
                    emit(Resource.Success(productDtoList.toDomainList()))
                } else {
                    // Handle case where body is null even on success (unlikely for lists)
                    emit(Resource.Error("Received empty product list from server."))
                }
            } else {
                // Handle API error (e.g., 4xx, 5xx)
                emit(Resource.Error("API Error: ${response.code()} ${response.message()}"))
            }
            // --- End Actual API Call ---
        } catch (e: HttpException) {
            // Handle errors in the HTTP response (non-2xx codes)
            emit(Resource.Error("Network error: ${e.localizedMessage ?: "HttpException"}"))
        } catch (e: IOException) {
            // Handle errors in network communication (no connection, timeout, etc.)
            emit(Resource.Error("Network unavailable. Please check connection."))
        } catch (e: Exception) {
            // Catch other potential errors (e.g., JSON parsing, mapping errors)
            emit(
                Resource.Error(
                    e.localizedMessage ?: "An unexpected error occurred fetching products"
                )
            )
        }
    }.flowOn(Dispatchers.IO)

    override fun getProductById(id: String): Flow<Resource<Product>> = flow {
        emit(Resource.Loading)
        try {
            // --- Actual API Call ---
            val response = apiService.getProductById(id)
            if (response.isSuccessful) {
                val dto = response.body()
                if (dto != null) {
                    val domainProduct = dto.toDomain()
                    if (domainProduct != null) {
                        emit(Resource.Success(domainProduct))
                    } else {
                        // Error during mapping DTO -> Domain
                        emit(Resource.Error("Failed to process product data for ID: $id"))
                    }
                } else {
                    // Handle case where body is null (could be 404 converted?)
                    emit(Resource.Error("Product with ID $id not found (empty response)."))
                }
            } else {
                // Handle API error (e.g., 404 Not Found)
                emit(Resource.Error("Product with ID $id not found (Error ${response.code()})."))
            }
            // --- End Actual API Call ---
        } catch (e: HttpException) {
            emit(Resource.Error("Network error fetching product $id: ${e.localizedMessage ?: "HttpException"}"))
        } catch (e: IOException) {
            emit(Resource.Error("Network unavailable fetching product $id."))
        } catch (e: Exception) {
            emit(
                Resource.Error(
                    e.localizedMessage ?: "An unexpected error occurred fetching product $id"
                )
            )
        }
    }.flowOn(Dispatchers.IO)
}