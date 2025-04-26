package com.example.tbcacademyfinal.domain.usecase.user

import com.example.tbcacademyfinal.domain.model.User
import com.example.tbcacademyfinal.domain.repository.UserRepository
import com.example.tbcacademyfinal.common.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CreateUserProfileUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(user: User): Flow<Resource<Unit>> {
        return userRepository.createUserProfile(user)
    }
}