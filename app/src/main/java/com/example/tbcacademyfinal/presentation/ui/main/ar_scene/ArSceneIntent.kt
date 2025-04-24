package com.example.tbcacademyfinal.presentation.ui.main.ar_scene

import com.example.tbcacademyfinal.presentation.model.CollectionItemUi

sealed interface ArSceneIntent {
    data object LoadCollection : ArSceneIntent
    data class SelectItemToPlace(val item: CollectionItemUi) : ArSceneIntent
    data object ItemPlaced : ArSceneIntent // User successfully placed the selected item
    data object ClearSelection : ArSceneIntent // User dismissed selection or placed item
    data object HidePlanes : ArSceneIntent // Plane found or item placed, hide renderer
    // Add intents for removing placed items, undo, etc.
}