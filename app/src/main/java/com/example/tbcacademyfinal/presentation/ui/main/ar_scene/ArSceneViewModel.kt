package com.example.tbcacademyfinal.presentation.ui.main.ar_scene

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.tbcacademyfinal.common.PhotoUploadWorker
import com.example.tbcacademyfinal.domain.usecase.collection.GetCollectionItemsUseCase
import com.example.tbcacademyfinal.presentation.mapper.toUiModelList
import com.example.tbcacademyfinal.presentation.model.CollectionItemUi
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ArSceneViewModel @Inject constructor(
    private val getCollectionItemsUseCase: GetCollectionItemsUseCase,
    private val workManager: WorkManager,
    private val firebaseAuth: FirebaseAuth,
    @ApplicationContext private val appContext: Context
) : ViewModel() {

    var state by mutableStateOf(ArSceneState())
        private set

    private val _event = MutableSharedFlow<ArSceneSideEffect>()
    val event: SharedFlow<ArSceneSideEffect> = _event.asSharedFlow()

    init {
        processIntent(ArSceneIntent.LoadCollection)
    }

    fun processIntent(intent: ArSceneIntent) {
        when (intent) {
            is ArSceneIntent.LoadCollection -> loadCollection()
            is ArSceneIntent.SelectItemToPlace -> selectItem(intent.item)
            is ArSceneIntent.ItemPlaced -> itemPlaced()
            is ArSceneIntent.ClearSelection -> clearSelection()
            is ArSceneIntent.HidePlanes -> hidePlanes()
            is ArSceneIntent.DiscardPhotoClicked -> discardPhoto()
            is ArSceneIntent.PhotoCaptured -> showPhotoPreview(intent.bitmap)
            is ArSceneIntent.TakePhotoButtonClicked -> {
                viewModelScope.launch {
                    _event.emit(ArSceneSideEffect.ShowSnackBar("ViewModel notified: Take photo initiated by UI"))
                }
            }

            is ArSceneIntent.UploadPhotoClicked -> uploadPhoto()
            ArSceneIntent.GoBackClicked -> navigateBack()
        }
    }

    private fun showPhotoPreview(bitmap: Bitmap) {
        state = state.copy(isPreviewingPhoto = true, capturedBitmap = bitmap)
    }

    private fun discardPhoto() {
        state = state.copy(isPreviewingPhoto = false, capturedBitmap = null)
    }

    private fun loadCollection() {
        viewModelScope.launch {
            getCollectionItemsUseCase()
                .onStart {
                    state =
                        state.copy(
                            isLoadingCollection = true,
                            collectionError = null
                        )

                }
                .catch { e ->
                    val errorMsg = e.localizedMessage ?: "Failed to load collection"
                    state =
                        state.copy(
                            isLoadingCollection = false,
                            collectionError = errorMsg
                        )

                    _event.emit(ArSceneSideEffect.ShowSnackBar(errorMsg))
                }
                .collect { domainItems ->
                    state =
                        state.copy(
                            isLoadingCollection = false,
                            availableItems = domainItems.toUiModelList(),
                            collectionError = null
                        )
                }
        }
    }

    private fun selectItem(item: CollectionItemUi) {
        Log.d("ArScreen", "Selecting item: ${item.name}, Model: ${item.modelFile}")
        state = state.copy(selectedItemModelFile = item.modelFile)
        updateInstructionText()
    }

    private fun itemPlaced() {
        state = state.copy(selectedItemModelFile = null)
        updateInstructionText()
    }

    private fun clearSelection() {
        state = state.copy(selectedItemModelFile = null)
        updateInstructionText()
    }

    private fun hidePlanes() {
        state = state.copy(showPlaneRenderer = false)
    }

    private fun updateInstructionText() {
        val currentState = state
        val text = when {
            currentState.trackingFailureReason != null -> "Tracking Lost: ${currentState.trackingFailureReason}"
            currentState.availableItems.isEmpty() && !currentState.isLoadingCollection -> "No items in collection"
            currentState.selectedItemModelFile != null -> "Tap a surface to place the item"
            !currentState.showPlaneRenderer -> "Tap to place another item or move existing ones"
            else -> "Point phone down to find a surface"
        }
        state = state.copy(instructionText = text)
    }

    private fun uploadPhoto() {
        val bitmapToUpload = state.capturedBitmap ?: return
        val currentUser = firebaseAuth.currentUser ?: return

        state = state.copy(
            isPreviewingPhoto = false,
            capturedBitmap = null,
            isUploading = true
        )


        viewModelScope.launch(Dispatchers.IO) {
            val cacheUri = saveBitmapToCache(appContext, bitmapToUpload)
            _event.emit(ArSceneSideEffect.ShowSnackBar("Uploading Photo"))
            if (cacheUri == null) {
                withContext(Dispatchers.Main) {
                    state = state.copy(isUploading = false)  // Clear indicator
                    _event.emit(ArSceneSideEffect.ShowSnackBar("Error saving photo for upload."))
                }
                return@launch
            }

            val filename = "ARDesign_${UUID.randomUUID()}.jpg"
            val uploadPath = "user_photos/${currentUser.uid}/$filename"

            val inputData = workDataOf(
                PhotoUploadWorker.KEY_FILE_URI to cacheUri.toString(),
                PhotoUploadWorker.KEY_UPLOAD_PATH to uploadPath
            )

            val uploadWorkRequest = OneTimeWorkRequestBuilder<PhotoUploadWorker>()
                .setInputData(inputData)
                .setConstraints(
                    Constraints(
                        requiredNetworkType = NetworkType.CONNECTED
                    )
                )
                .addTag("photo_upload")
                .build()

            workManager.enqueueUniqueWork(
                "upload_$filename",
                ExistingWorkPolicy.KEEP,
                uploadWorkRequest
            )

            observeUploadWork(uploadWorkRequest.id)

        }
    }

    private fun observeUploadWork(workId: UUID) {
        workManager.getWorkInfoByIdLiveData(workId).observeForever { workInfo ->
            when (workInfo?.state) {
                WorkInfo.State.SUCCEEDED -> {
                    val downloadUrl =
                        workInfo.outputData.getString(PhotoUploadWorker.KEY_RESULT_URL)
                    showMessage("Upload successful: $downloadUrl")
                    state = state.copy(isUploading = false)
                }

                WorkInfo.State.FAILED -> {
                    val errorMsg = workInfo.outputData.getString(PhotoUploadWorker.KEY_RESULT_ERROR)
                    showMessage("Upload failed: $errorMsg")
                    state = state.copy(isUploading = false)
                    // Clean up observer?
                }

                WorkInfo.State.CANCELLED -> {
                    showMessage("Upload cancelled")
                    state = state.copy(isUploading = false)
                }

                WorkInfo.State.RUNNING -> {
                    state = state.copy(isUploading = true)
                }

                else -> {}
            }
        }
    }

    private fun saveBitmapToCache(context: Context, bitmap: Bitmap): Uri? {
        val cachePath = File(context.cacheDir, "image_cache")
        cachePath.mkdirs()
        val fileName = "upload_temp_${System.currentTimeMillis()}.jpg"
        val file = File(cachePath, fileName)
        return try {
            FileOutputStream(file).use { fos ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos)
            }
            Uri.fromFile(file)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    private fun showMessage(message: String) {
        viewModelScope.launch {
            _event.emit(ArSceneSideEffect.ShowSnackBar(message))
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            _event.emit(ArSceneSideEffect.NavigateBack)
        }
    }

    override fun onCleared() {
        super.onCleared()
    }

}