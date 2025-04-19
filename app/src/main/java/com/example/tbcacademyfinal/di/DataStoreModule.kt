package com.example.tbcacademyfinal.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val USER_PREFERENCES = "user_preferences"

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun providePreferencesDataStore(
        @ApplicationContext appContext: Context
        // Potential: Inject CoroutineScope if needed for specific configurations
    ): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            // Optional: Add migrations if needed later
            // Optional: Provide a specific CoroutineScope if defaults aren't sufficient
            // scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            produceFile = { appContext.preferencesDataStoreFile(USER_PREFERENCES) }
        )
    }

    // You might provide specific Preference keys here later if needed globally,
    // but often they are defined closer to where they are used (e.g., in a Repository)
}