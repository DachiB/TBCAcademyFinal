package com.example.tbcacademyfinal.common.safecalls

import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException


suspend fun <T> safeApiCall(
    apiCall: suspend () -> Response<T>,
    onSuccess: suspend (T) -> Unit,
    onError: suspend (String) -> Unit
) {
    try {
        val response = apiCall()
        if (response.isSuccessful) {
            response.body()?.let {
                onSuccess(it)
            } ?: onError("No data available")
        } else {
            onError(response.errorBody()?.string() ?: "API error")
        }
    } catch (e: HttpException) {
        onError("HTTP error ${e.code()}: ${e.message()}")
    } catch (e: IOException) {
        onError("Network error: ${e.localizedMessage}")
    } catch (e: Exception) {
        onError("Unexpected error: ${e.localizedMessage}")
    }
}