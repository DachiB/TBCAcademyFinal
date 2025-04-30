package com.example.tbcacademyfinal.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.booleanPreferencesKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.File

private object PreferencesKeys {
    val SEEN_LANDING = booleanPreferencesKey("seen_landing")
    val SHOULD_REMEMBER_USER = booleanPreferencesKey("should_remember_user")
}

class DataStoreRepositoryTest {

    private lateinit var dataStore: DataStore<Preferences>
    private lateinit var repository: DataStoreRepositoryImpl

    private val testDispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        dataStore = PreferenceDataStoreFactory.create(
            scope = CoroutineScope(testDispatcher),
            produceFile = { File.createTempFile("test_datastore", ".preferences_pb") }
        )

        repository = DataStoreRepositoryImpl(dataStore)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `default hasSeenLanding is false`() = runTest {
        assertFalse(repository.hasSeenLanding().first())
    }

    @Test
    fun `setHasSeenLanding(true) persists true`() = runTest {
        repository.setHasSeenLanding(true)
        testDispatcher.scheduler.advanceUntilIdle()
        assertTrue(repository.hasSeenLanding().first())
    }

    @Test
    fun `default shouldRememberUser is false`() = runTest {
        assertFalse(repository.shouldRememberUser().first())
    }

    @Test
    fun `setShouldRememberUser(true) persists true`() = runTest {
        repository.setShouldRememberUser(true)
        testDispatcher.scheduler.advanceUntilIdle()
        assertTrue(repository.shouldRememberUser().first())
    }
}