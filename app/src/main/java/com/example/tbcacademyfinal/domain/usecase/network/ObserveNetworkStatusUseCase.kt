package com.example.tbcacademyfinal.domain.usecase.network

import com.example.tbcacademyfinal.domain.util.ConnectivityObserver
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveNetworkStatusUseCase @Inject constructor(
    private val connectivityObserver: ConnectivityObserver
) {
    operator fun invoke(): Flow<ConnectivityObserver.Status> {
        return connectivityObserver.observe()
    }
}