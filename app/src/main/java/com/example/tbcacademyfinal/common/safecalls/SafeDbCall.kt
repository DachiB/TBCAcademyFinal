package com.example.tbcacademyfinal.common.safecalls

import com.example.tbcacademyfinal.common.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

suspend fun safeDbCall(
    block: suspend () -> Unit
): Resource<Unit> = withContext(Dispatchers.IO) {
    runCatching {
        block()
    }.fold(
        onSuccess  = { Resource.Success(Unit) },
        onFailure  = { e ->
            Resource.Error(
                message = "Database error: ${e.localizedMessage}",
                exception = e as? IOException
            )
        }
    )
}