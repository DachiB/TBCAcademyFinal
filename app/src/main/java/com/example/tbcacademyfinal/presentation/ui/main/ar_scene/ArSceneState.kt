package com.example.tbcacademyfinal.presentation.ui.main.ar_scene

import androidx.compose.runtime.Immutable
import com.example.tbcacademyfinal.presentation.model.CollectionItemUi
import com.google.ar.core.TrackingFailureReason

@Immutable
data class ArSceneState(
    // State for collection items available to place
    val availableItems: List<CollectionItemUi> = emptyList(),
    val selectedItemModelFile: String? = null, // Path of the model currently selected for placement
    val isLoadingCollection: Boolean = true, // Loading state for the collection list
    val collectionError: String? = null, // Error state for the collection list

    // State related to AR session itself
    val trackingFailureReason: TrackingFailureReason? = null,
    val instructionText: String = "Point phone down to find a surface", // Default instruction
    val showPlaneRenderer: Boolean = true // Show/hide planes

    // Add other states as needed, e.g., number of placed objects
)