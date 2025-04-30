package com.example.tbcacademyfinal.domain.repository

import android.net.Uri
import com.example.tbcacademyfinal.common.Resource
import kotlinx.coroutines.flow.Flow

interface StorageRepository {
    suspend fun uploadImage(localFileUri: Uri, destinationPath: String): Resource<String>
    fun getUserPhotos(userId: String): Flow<Resource<List<Uri>>>
    suspend fun deleteImage(photoUrl: String): Resource<Unit>
}
