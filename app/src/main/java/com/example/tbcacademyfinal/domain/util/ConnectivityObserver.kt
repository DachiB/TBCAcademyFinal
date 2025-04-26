package com.example.tbcacademyfinal.domain.util

import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {
    // Emits true if network is available, false otherwise
    fun observe(): Flow<Status>

    // Optional: Function to get current status immediately
    enum class Status {
        Available, Unavailable, Losing, Lost
    }
}