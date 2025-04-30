package com.example.tbcacademyfinal.common.safecalls

import com.example.tbcacademyfinal.common.Resource
import com.google.firebase.storage.StorageException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

suspend fun <T> safeStorageCall(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    action: suspend () -> T
): Resource<T> = withContext(dispatcher) {
    runCatching { action() }
        .fold(
            onSuccess  = { Resource.Success(it) },
            onFailure  = { e ->
                val msg = when (e) {
                    is IOException -> "Network error: ${e.localizedMessage}"
                    is StorageException -> "Storage error (${e.errorCode}): ${e.localizedMessage}"
                    is IllegalArgumentException -> "Invalid URL: ${e.localizedMessage}"
                    else              -> "Unexpected error: ${e.localizedMessage}"
                }
                Resource.Error(msg, e as? Exception)
            }
        )
}