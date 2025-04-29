package com.example.tbcacademyfinal.common

import android.content.Context
import androidx.core.net.toUri
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.tbcacademyfinal.domain.repository.StorageRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

@HiltWorker
class PhotoUploadWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val storageRepository: StorageRepository
) : CoroutineWorker(appContext, workerParams) {

    companion object {
        const val KEY_FILE_URI = "file_uri"
        const val KEY_UPLOAD_PATH = "upload_path"
        const val KEY_RESULT_URL = "result_url" // Key for output data
        const val KEY_RESULT_ERROR = "result_error" // Key for error message
    }

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val fileUriString = inputData.getString(KEY_FILE_URI) ?: return@withContext Result.failure(
            workDataOf(KEY_RESULT_ERROR to "File URI missing")
        )
        val uploadPath = inputData.getString(KEY_UPLOAD_PATH) ?: return@withContext Result.failure(
            workDataOf(KEY_RESULT_ERROR to "Upload path missing")
        )

        val fileUri = fileUriString.toUri()

        val uploadResource = storageRepository.uploadImage(fileUri, uploadPath)

        try {
            applicationContext.contentResolver.delete(
                fileUri,
                null,
                null
            )
            val cacheFile = File(fileUri.path ?: "")
            if (cacheFile.exists() && cacheFile.path.contains(applicationContext.cacheDir.path)) {
                cacheFile.delete()
            }
        } catch (e: Exception) {
            println("Error deleting cache file $fileUriString: ${e.message}")
        }

        when (uploadResource) {
            is Resource.Success -> Result.success(
                workDataOf(KEY_RESULT_URL to uploadResource.data)
            )

            is Resource.Error -> Result.failure(
                workDataOf(KEY_RESULT_ERROR to uploadResource.message)
            )

            is Resource.Loading -> Result.retry()
        }
    }
}