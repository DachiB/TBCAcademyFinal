package com.example.tbcacademyfinal.domain.usecase.user

import android.net.Uri
import com.example.tbcacademyfinal.common.Resource
import com.example.tbcacademyfinal.domain.repository.StorageRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetUserPhotosUseCase @Inject constructor(
    private val storageRepository: StorageRepository,
    private val firebaseAuth: FirebaseAuth // Inject to get current user ID
) {
    operator fun invoke(): Flow<Resource<List<String>>> {
        val currentUser = firebaseAuth.currentUser
        return if (currentUser != null) {
            storageRepository.getUserPhotos(currentUser.uid).map { resource ->
                when (resource) {
                    is Resource.Success -> Resource.Success(resource.data.map { it.toString() })
                    is Resource.Error -> Resource.Error(resource.message, resource.exception)
                    is Resource.Loading -> Resource.Loading
                }}
        } else {
            flow { emit(Resource.Error("User not logged in.")) }
        }
    }
}