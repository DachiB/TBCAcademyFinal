package com.example.tbcacademyfinal.domain.repository

import com.example.tbcacademyfinal.domain.model.User
import com.example.tbcacademyfinal.common.Resource
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun createUserProfile(user: User): Flow<Resource<Unit>>

    suspend fun getUserProfile(uid: String): Flow<Resource<User>>
}
