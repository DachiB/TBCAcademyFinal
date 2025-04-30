package com.example.tbcacademyfinal.domain.usecase.user


import com.example.tbcacademyfinal.domain.model.User
import com.example.tbcacademyfinal.domain.repository.AuthRepository
import com.example.tbcacademyfinal.domain.repository.UserRepository
import com.example.tbcacademyfinal.common.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository // Inject User Repo
) {
    suspend operator fun invoke(): Flow<Resource<User>> {
        val firebaseUser = authRepository.getCurrentUser()
            ?:
            return flow { emit(Resource.Error("No authenticated user found.")) }
        return userRepository.getUserProfile(firebaseUser.uid)
    }
}