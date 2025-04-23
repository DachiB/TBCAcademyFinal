package com.example.tbcacademyfinal.domain.repository

import com.example.tbcacademyfinal.presentation.misc.LanguageType
import com.example.tbcacademyfinal.presentation.misc.ThemeType
import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {
    suspend fun setHasSeenLanding(hasSeen: Boolean)
    fun hasSeenLanding(): Flow<Boolean>

    // --- Add Theme Methods ---
    suspend fun setTheme(theme: ThemeType)
    fun getTheme(): Flow<ThemeType> // Return the enum
    // --- End Theme Methods ---

    // --- Add Language Methods ---
    suspend fun setLanguage(language: LanguageType)
    fun getLanguage(): Flow<LanguageType> // Return the enum
    // --- End Language Methods ---
}