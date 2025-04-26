package com.example.tbcacademyfinal.domain.usecase.user


import com.example.tbcacademyfinal.domain.model.User
import com.example.tbcacademyfinal.domain.repository.AuthRepository
import com.example.tbcacademyfinal.domain.repository.UserRepository
import com.example.tbcacademyfinal.common.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

// Simple use case for now, could return a domain User model later
class GetUserUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository // Inject User Repo
) {
    // Now returns a Flow of Resource<User> (domain model)
    suspend operator fun invoke(): Flow<Resource<User>> {
        val firebaseUser = authRepository.getCurrentUser()
            ?: // Return an error flow if no user is logged in
            return flow { emit(Resource.Error("No authenticated user found.")) }
        // Fetch the profile from Firestore using the UID
        return userRepository.getUserProfile(firebaseUser.uid)
    }
}