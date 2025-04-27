package com.example.tbcacademyfinal.domain.repository

import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {
    suspend fun setHasSeenLanding(hasSeen: Boolean)
    fun hasSeenLanding(): Flow<Boolean>

    suspend fun setShouldRememberUser(remember: Boolean)
    fun shouldRememberUser(): Flow<Boolean>

    fun getAppTheme(): Flow<Boolean>
    suspend fun updateTheme(isDark: Boolean)

    suspend fun updateLanguage(language: String)
    fun getAppLanguage(): Flow<String>

}