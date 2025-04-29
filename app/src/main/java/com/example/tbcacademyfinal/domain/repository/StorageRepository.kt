package com.example.tbcacademyfinal.domain.repository

import android.net.Uri
import com.example.tbcacademyfinal.common.Resource

interface StorageRepository {
    suspend fun uploadImage(localFileUri: Uri, destinationPath: String): Resource<String>
}
