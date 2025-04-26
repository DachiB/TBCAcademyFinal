package com.example.tbcacademyfinal.domain.usecase.user

import com.example.tbcacademyfinal.domain.repository.AuthRepository
import com.example.tbcacademyfinal.domain.repository.DataStoreRepository
import javax.inject.Inject

class LogOutUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val dataStoreRepository: DataStoreRepository
) {
    suspend operator fun invoke() {
        authRepository.logout()
        dataStoreRepository.setShouldRememberUser(false)
        // Potentially clear other user-specific data/caches here if needed
    }
}