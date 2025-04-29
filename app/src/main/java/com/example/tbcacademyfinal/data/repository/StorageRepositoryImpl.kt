package com.example.tbcacademyfinal.data.repository


import android.net.Uri
import com.example.tbcacademyfinal.common.Resource
import com.example.tbcacademyfinal.domain.repository.StorageRepository
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.io.IOException
import javax.inject.Inject

class StorageRepositoryImpl @Inject constructor(
    private val storage: FirebaseStorage // Inject FirebaseStorage
) : StorageRepository {

    override suspend fun uploadImage(localFileUri: Uri, destinationPath: String): Resource<String> {
        return try {
            val storageRef = storage.reference.child(destinationPath)
            val uploadTask = storageRef.putFile(localFileUri).await() // Suspend until upload completes

            // Get download URL after successful upload
            val downloadUrl = uploadTask.storage.downloadUrl.await()?.toString()

            if (downloadUrl != null) {
                Resource.Success(downloadUrl)
            } else {
                Resource.Error("Upload succeeded but failed to get download URL.")
            }
        } catch (e: IOException) {
            Resource.Error("Network error during upload: ${e.localizedMessage}", e)
        } catch (e: Exception) {
            Resource.Error("Failed to upload image: ${e.localizedMessage}", e)
        }
    }
}