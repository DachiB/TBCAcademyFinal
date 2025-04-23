package com.example.tbcacademyfinal.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import com.example.tbcacademyfinal.data.local.PreferencesKeys
import com.example.tbcacademyfinal.domain.repository.DataStoreRepository
import com.example.tbcacademyfinal.presentation.misc.LanguageType
import com.example.tbcacademyfinal.presentation.misc.ThemeType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class DataStoreRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : DataStoreRepository {

    override suspend fun setHasSeenLanding(hasSeen: Boolean) {
        try {
            dataStore.edit { preferences ->
                preferences[PreferencesKeys.HAS_SEEN_LANDING] = hasSeen
            }
        } catch (e: IOException) {
            // Handle error, e.g., log it
            println("Error saving landing preference: ${e.localizedMessage}")
        }
    }

    override fun hasSeenLanding(): Flow<Boolean> {
        return dataStore.data
            .catch { exception ->
                // IOException means Preferences couldn't be read, emit empty
                if (exception is IOException) {
                    println("Error reading landing preference: ${exception.localizedMessage}")
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                // Default to false if the key doesn't exist
                preferences[PreferencesKeys.HAS_SEEN_LANDING] ?: false
            }
    }

    override suspend fun setTheme(theme: ThemeType) {
        try {
            dataStore.edit { preferences ->
                preferences[PreferencesKeys.APP_THEME] = theme.name // Store enum name
            }
        } catch (e: IOException) {
            println("Error saving theme preference: ${e.localizedMessage}")
        }
    }

    override fun getTheme(): Flow<ThemeType> {
        return dataStore.data
            .catch { exception -> // Same catch block as before
                if (exception is IOException) {
                    println("Error reading theme preference: ${exception.localizedMessage}")
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                // Get the stored string, default to SYSTEM theme's name if null/invalid
                val themeName = preferences[PreferencesKeys.APP_THEME] ?: ThemeType.SYSTEM.name
                try {
                    ThemeType.valueOf(themeName) // Convert string back to enum
                } catch (e: IllegalArgumentException) {
                    println("Invalid theme name found in DataStore: $themeName. Defaulting to SYSTEM.")
                    ThemeType.SYSTEM // Default if stored value is invalid
                }
            }
    }

    override suspend fun setLanguage(language: LanguageType) {
        try {
            dataStore.edit { preferences ->
                preferences[PreferencesKeys.APP_LANGUAGE] = language.name // Store enum name
            }
        } catch (e: IOException) {
            println("Error saving language preference: ${e.localizedMessage}")
        }
    }

    override fun getLanguage(): Flow<LanguageType> {
        return dataStore.data
            .catch { exception -> // Same catch block
                if (exception is IOException) {
                    println("Error reading language preference: ${exception.localizedMessage}")
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                // Get the stored string, default to ENGLISH if null/invalid
                val langName = preferences[PreferencesKeys.APP_LANGUAGE] ?: LanguageType.ENGLISH.name
                try {
                    LanguageType.valueOf(langName) // Convert string back to enum
                } catch (e: IllegalArgumentException) {
                    println("Invalid language name found in DataStore: $langName. Defaulting to ENGLISH.")
                    LanguageType.ENGLISH // Default if stored value is invalid
                }
            }
    }
}