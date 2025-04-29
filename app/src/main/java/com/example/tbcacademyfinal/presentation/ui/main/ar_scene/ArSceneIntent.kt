package com.example.tbcacademyfinal.presentation.ui.main.ar_scene

import android.graphics.Bitmap
import com.example.tbcacademyfinal.presentation.model.CollectionItemUi

sealed interface ArSceneIntent {
    data object LoadCollection : ArSceneIntent
    data class SelectItemToPlace(val item: CollectionItemUi) : ArSceneIntent
    data object ItemPlaced : ArSceneIntent
    data object ClearSelection : ArSceneIntent
    data object HidePlanes : ArSceneIntent

    data object TakePhotoButtonClicked : ArSceneIntent
    data class PhotoCaptured(val bitmap: Bitmap) : ArSceneIntent
    data object DiscardPhotoClicked : ArSceneIntent
    data object UploadPhotoClicked : ArSceneIntent

    data object GoBackClicked : ArSceneIntent
}