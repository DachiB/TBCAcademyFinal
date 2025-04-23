package com.example.tbcacademyfinal.domain.repository

import com.example.tbcacademyfinal.domain.model.User
import com.example.tbcacademyfinal.util.Resource
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    // Creates user profile document in Firestore
    suspend fun createUserProfile(user: User): Flow<Resource<Unit>>

    // Gets user profile document from Firestore (can be used by GetUserUseCase later)
    suspend fun getUserProfile(uid: String): Flow<Resource<User>>

    // Optional: Update user profile
    // suspend fun updateUserProfile(user: User): Flow<Resource<Unit>>
}
