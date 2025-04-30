package com.example.tbcacademyfinal.data.repository

import android.net.Uri
import com.example.tbcacademyfinal.common.Resource
import com.example.tbcacademyfinal.common.safecalls.safeStorageCall
import com.example.tbcacademyfinal.domain.repository.StorageRepository
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class StorageRepositoryImpl @Inject constructor(
    private val storage: FirebaseStorage
) : StorageRepository {

    override suspend fun uploadImage(
        localFileUri: Uri,
        destinationPath: String
    ): Resource<String> = safeStorageCall {
        val ref = storage.reference.child(destinationPath)
        ref.putFile(localFileUri).await()
        ref.downloadUrl.await().toString()
    }

    override fun getUserPhotos(userId: String): Flow<Resource<List<Uri>>> = flow {
        emit(Resource.Loading)
        val result: Resource<List<Uri>> = safeStorageCall {
            storage
                .reference
                .child("user_photos/$userId")
                .listAll()
                .await()
                .items
                .map { it.downloadUrl.await() }
        }
        emit(result)
    }.flowOn(Dispatchers.IO)

    override suspend fun deleteImage(photoUrl: String): Resource<Unit> = safeStorageCall {
        storage.getReferenceFromUrl(photoUrl).delete().await()
    }
}
