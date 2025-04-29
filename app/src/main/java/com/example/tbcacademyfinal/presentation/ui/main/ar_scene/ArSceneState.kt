package com.example.tbcacademyfinal.presentation.ui.main.ar_scene

import android.graphics.Bitmap
import androidx.compose.runtime.Immutable
import com.example.tbcacademyfinal.presentation.model.CollectionItemUi
import com.google.ar.core.TrackingFailureReason

@Immutable
data class ArSceneState(
    val availableItems: List<CollectionItemUi> = emptyList(),
    val selectedItemModelFile: String? = null,
    val isLoadingCollection: Boolean = true,
    val collectionError: String? = null,

    val trackingFailureReason: TrackingFailureReason? = null,
    val instructionText: String = "Point phone down to find a surface",
    val showPlaneRenderer: Boolean = true,

    val isPreviewingPhoto: Boolean = false,
    val capturedBitmap: Bitmap? = null,
    val isUploading: Boolean = false
)