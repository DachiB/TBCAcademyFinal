package com.example.tbcacademyfinal.common.safecalls

import com.example.tbcacademyfinal.common.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private suspend fun <T> safeFirestoreCall(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    block: suspend () -> T
): Resource<T> = withContext(dispatcher) {
    runCatching { block() }.fold(
        onSuccess  = { Resource.Success(it) },
        onFailure  = { e ->
            Resource.Error(
                message   = e.localizedMessage ?: "Firestore error",
                exception = e as? Exception
            )
        }
    )
}