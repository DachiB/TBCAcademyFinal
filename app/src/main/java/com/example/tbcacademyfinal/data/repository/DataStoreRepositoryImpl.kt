package com.example.tbcacademyfinal.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import com.example.tbcacademyfinal.data.local.keys.PreferencesKeys
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
}