package com.example.tbcacademyfinal.domain.repository

import com.example.tbcacademyfinal.common.Resource
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun login(email: String, password: String): Flow<Resource<AuthResult>>
    fun register(email: String, password: String): Flow<Resource<AuthResult>>
    suspend fun getCurrentUser(): FirebaseUser?
    suspend fun logout()
}