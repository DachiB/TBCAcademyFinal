package com.example.tbcacademyfinal.domain.repository

import com.example.tbcacademyfinal.util.Resource
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    // Optional: Function to get current user state if needed elsewhere
    // fun getCurrentUser(): FirebaseUser?

    // Listen to auth state changes (useful for automatic login/logout updates)
    // val authState: Flow<FirebaseUser?>

    suspend fun login(email: String, password: String): Flow<Resource<AuthResult>>
    suspend fun register(email: String, password: String): Flow<Resource<AuthResult>>
    suspend fun logout()
}