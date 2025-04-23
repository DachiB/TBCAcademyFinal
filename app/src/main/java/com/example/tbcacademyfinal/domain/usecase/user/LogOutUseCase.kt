package com.example.tbcacademyfinal.domain.usecase.user

import com.example.tbcacademyfinal.domain.repository.AuthRepository
import javax.inject.Inject

class LogOutUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke() {
        authRepository.logout()
        // Potentially clear other user-specific data/caches here if needed
    }
}