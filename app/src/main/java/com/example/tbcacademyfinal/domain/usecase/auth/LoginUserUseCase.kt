package com.example.tbcacademyfinal.domain.usecase.auth

import com.example.tbcacademyfinal.domain.repository.AuthRepository
import com.example.tbcacademyfinal.common.Resource
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginUserUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(email: String, password: String): Flow<Resource<AuthResult>> {
        return authRepository.login(email, password)
    }
}