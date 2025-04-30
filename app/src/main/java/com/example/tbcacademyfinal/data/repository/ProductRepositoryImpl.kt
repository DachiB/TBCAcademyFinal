package com.example.tbcacademyfinal.data.repository

import com.example.tbcacademyfinal.data.mapper.toDomain
import com.example.tbcacademyfinal.data.mapper.toDomainList
import com.example.tbcacademyfinal.data.remote.ProductApiService
import com.example.tbcacademyfinal.domain.model.Product
import com.example.tbcacademyfinal.domain.repository.ProductRepository
import com.example.tbcacademyfinal.common.Resource
import com.example.tbcacademyfinal.common.safecalls.safeApiCall
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
        safeApiCall(
            apiCall = { apiService.getProducts() },
            onSuccess = { dtoList ->
                val domainList = dtoList.toDomainList()
                emit(Resource.Success(domainList))
            },
            onError = { errorMessage ->
                emit(Resource.Error(errorMessage))
            }
        )
    }.flowOn(Dispatchers.IO)

    override fun getProductById(id: String): Flow<Resource<Product>> = flow {
        emit(Resource.Loading)
        try {
            val response = apiService.getProductById(id)
            if (response.isSuccessful) {
                val dto = response.body()
                if (dto != null) {
                    val domainProduct = dto.toDomain()
                    if (domainProduct != null) {
                        emit(Resource.Success(domainProduct))
                    } else {
                        emit(Resource.Error("Failed to process product data for ID: $id"))
                    }
                } else {
                    emit(Resource.Error("Product with ID $id not found (empty response)."))
                }
            } else {
                emit(Resource.Error("Product with ID $id not found (Error ${response.code()})."))
            }
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