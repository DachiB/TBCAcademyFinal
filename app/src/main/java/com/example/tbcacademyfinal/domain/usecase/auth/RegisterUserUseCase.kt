package com.example.tbcacademyfinal.domain.usecase.auth

import com.example.tbcacademyfinal.common.Resource
import com.example.tbcacademyfinal.domain.model.User
import com.example.tbcacademyfinal.domain.repository.AuthRepository
import com.example.tbcacademyfinal.domain.usecase.user.CreateUserProfileUseCase
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class RegisterUserUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val createUserProfile: CreateUserProfileUseCase
) {
    operator fun invoke(
        email: String,
        password: String
    ): Flow<Resource<AuthResult>> = flow {
        authRepository
            .register(email, password)
            .collect { authRes ->
                when (authRes) {
                    is Resource.Loading -> {
                        emit(Resource.Loading)
                    }
                    is Resource.Error -> {
                        emit(Resource.Error(authRes.message, authRes.exception))
                    }
                    is Resource.Success -> {
                        val firebaseUser = authRes.data.user
                        if (firebaseUser == null) {
                            emit(Resource.Error("Registration succeeded but user data was null."))
                        } else {
                            val newUser = User(
                                uid       = firebaseUser.uid,
                                email     = email,
                                createdAt = System.currentTimeMillis()
                            )

                            emit(Resource.Loading)

                            createUserProfile(newUser).collect { profileRes ->
                                when (profileRes) {
                                    is Resource.Loading -> {}

                                    is Resource.Success ->
                                        emit(Resource.Success(authRes.data))

                                    is Resource.Error ->
                                        emit(
                                            Resource.Error(
                                                message   = "Registered, but profile failed: ${profileRes.message}",
                                                exception = profileRes.exception
                                            )
                                        )
                                }
                            }
                        }
                    }
                }
            }
    }
        .flowOn(Dispatchers.IO)
}