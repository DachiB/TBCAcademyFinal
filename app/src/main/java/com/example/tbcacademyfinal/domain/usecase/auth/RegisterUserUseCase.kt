package com.example.tbcacademyfinal.domain.usecase.auth

import com.example.tbcacademyfinal.domain.model.User
import com.example.tbcacademyfinal.domain.repository.AuthRepository
import com.example.tbcacademyfinal.domain.usecase.user.CreateUserProfileUseCase
import com.example.tbcacademyfinal.common.Resource
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat // To chain flows
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class RegisterUserUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val createUserProfileUseCase: CreateUserProfileUseCase
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    suspend operator fun invoke(email: String, password: String): Flow<Resource<AuthResult>> {
        return authRepository.register(email, password).flatMapConcat { authResource ->
            when (authResource) {
                is Resource.Success -> {
                    val firebaseUser = authResource.data.user
                    if (firebaseUser != null) {
                        val newUser = User(
                            uid = firebaseUser.uid,
                            email = email,
                            createdAt = System.currentTimeMillis()
                        )
                        createUserProfileUseCase(newUser).map { profileResource ->
                            when (profileResource) {
                                is Resource.Success -> Resource.Success(authResource.data)
                                is Resource.Loading -> Resource.Success(authResource.data) // Or Resource.Loading? Decided Success is better UX
                                is Resource.Error -> Resource.Error(
                                    message = "User registered, but failed to create profile: ${profileResource.message}",
                                    exception = profileResource.exception
                                )
                            }
                        }
                    } else {
                        flow { emit(Resource.Error("Registration succeeded but user data was null.")) }
                    }
                }
                is Resource.Error -> flow {
                    emit(
                        Resource.Error(
                            authResource.message,
                            authResource.exception
                        )
                    )
                }

                is Resource.Loading -> flow { emit(Resource.Loading) }
            }
        }
    }
}