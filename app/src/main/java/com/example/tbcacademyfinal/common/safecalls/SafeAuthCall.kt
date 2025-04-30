package com.example.tbcacademyfinal.common.safecalls

import com.example.tbcacademyfinal.common.Resource
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun <T> safeAuthCall(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    action: suspend () -> T
): Resource<T> = withContext(dispatcher) {
    runCatching { action() }.fold(
        onSuccess = { Resource.Success(it) },
        onFailure = { e ->
            val msg = when (e) {
                is FirebaseAuthInvalidCredentialsException ->
                    "Invalid email or password."

                is FirebaseAuthUserCollisionException ->
                    "An account with this email already exists."

                else ->
                    e.localizedMessage ?: "An unexpected error occurred."
            }
            Resource.Error(msg, e as? Exception)
        }
    )
}