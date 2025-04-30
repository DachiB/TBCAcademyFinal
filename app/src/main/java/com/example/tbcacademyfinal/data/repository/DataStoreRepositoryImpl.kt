package com.example.tbcacademyfinal.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import com.example.tbcacademyfinal.data.local.keys.PreferencesKeys
import com.example.tbcacademyfinal.data.local.keys.PreferencesKeys.DARK_THEME_KEY
import com.example.tbcacademyfinal.data.local.keys.PreferencesKeys.LANGUAGE_KEY
import com.example.tbcacademyfinal.domain.repository.DataStoreRepository
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
                preferences[PreferencesKeys.SEEN_LANDING] = hasSeen
            }
        } catch (e: IOException) {
            println("Error saving landing preference: ${e.localizedMessage}")
        }
    }

    override fun hasSeenLanding(): Flow<Boolean> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    println("Error reading landing preference: ${exception.localizedMessage}")
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                preferences[PreferencesKeys.SEEN_LANDING] ?: false
            }
    }

    override suspend fun setShouldRememberUser(remember: Boolean) {
        try {
            dataStore.edit { prefs -> prefs[PreferencesKeys.SHOULD_REMEMBER_USER] = remember }
        } catch (e: IOException) { //TO-DO:
        }
    }

    override fun shouldRememberUser(): Flow<Boolean> {
        return dataStore.data.map { prefs ->
            prefs[PreferencesKeys.SHOULD_REMEMBER_USER] ?: false
        }
    }

    override fun getAppTheme(): Flow<Boolean> =
        dataStore.data
            .map { prefs -> prefs[DARK_THEME_KEY] == true }

    override suspend fun updateTheme(isDark: Boolean) {
        dataStore.edit { prefs ->
            prefs[DARK_THEME_KEY] = isDark
        }
    }

    override suspend fun updateLanguage(language: String) {
        dataStore.edit { preferences ->
            preferences[LANGUAGE_KEY] = language
        }
    }

    override fun getAppLanguage(): Flow<String> {
        return dataStore.data
            .map { preferences ->
                preferences[LANGUAGE_KEY] ?: "en"
            }
    }
}