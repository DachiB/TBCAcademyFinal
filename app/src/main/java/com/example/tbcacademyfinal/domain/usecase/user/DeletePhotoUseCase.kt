package com.example.tbcacademyfinal.domain.usecase.user

import com.example.tbcacademyfinal.common.Resource
import com.example.tbcacademyfinal.domain.repository.StorageRepository
import javax.inject.Inject

class DeletePhotoUseCase @Inject constructor(
    private val storageRepository: StorageRepository
) {
    suspend operator fun invoke(photoUrl: String): Resource<Unit> {
        if (photoUrl.isBlank()) {
            return Resource.Error("Photo URL cannot be empty.")
        }
        return storageRepository.deleteImage(photoUrl)
    }
}