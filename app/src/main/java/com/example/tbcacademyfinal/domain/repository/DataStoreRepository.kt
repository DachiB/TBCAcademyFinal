package com.example.tbcacademyfinal.domain.repository

import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {
    suspend fun setHasSeenLanding(hasSeen: Boolean)
    fun hasSeenLanding(): Flow<Boolean>
}