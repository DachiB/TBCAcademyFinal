package com.example.tbcacademyfinal.presentation.ui.main.ar_scene

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tbcacademyfinal.domain.usecase.collection.GetCollectionItemsUseCase
import com.example.tbcacademyfinal.presentation.mapper.toUiModelList
import com.example.tbcacademyfinal.presentation.model.CollectionItemUi
import com.google.ar.core.TrackingFailureReason
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArSceneViewModel @Inject constructor(
    private val getCollectionItemsUseCase: GetCollectionItemsUseCase
    // Inject other use cases later if needed (e.g., analytics)
) : ViewModel() {

    private val _state = MutableStateFlow(ArSceneState())
    val state: StateFlow<ArSceneState> = _state.asStateFlow()

    private val _event = MutableSharedFlow<ArSceneSideEffect>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
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
        }
    }

    // --- Public function to be called from ARScene callbacks ---
    fun onTrackingFailureChanged(reason: TrackingFailureReason?) {
        _state.update { it.copy(trackingFailureReason = reason) }
        updateInstructionText() // Update text based on tracking
    }

    // --- Intent Processors ---
    private fun loadCollection() {
        viewModelScope.launch {
            getCollectionItemsUseCase()
                .onStart {
                    _state.update {
                        it.copy(
                            isLoadingCollection = true,
                            collectionError = null
                        )
                    }
                }
                .catch { e ->
                    val errorMsg = e.localizedMessage ?: "Failed to load collection"
                    _state.update {
                        it.copy(
                            isLoadingCollection = false,
                            collectionError = errorMsg
                        )
                    }
                    _event.tryEmit(ArSceneSideEffect.ShowError(errorMsg))
                }
                .collect { domainItems ->
                    _state.update {
                        it.copy(
                            isLoadingCollection = false,
                            availableItems = domainItems.toUiModelList(),
                            collectionError = null
                        )
                    }
                }
        }
    }

    private fun selectItem(item: CollectionItemUi) {
        _state.update { it.copy(selectedItemModelFile = item.modelFile) }
        updateInstructionText() // Update text to prompt placement
    }

    private fun itemPlaced() {
        // Item successfully placed, clear selection for next placement
        _state.update { it.copy(selectedItemModelFile = null) }
        updateInstructionText() // Update text to prompt selection or movement
    }

    private fun clearSelection() {
        _state.update { it.copy(selectedItemModelFile = null) }
        updateInstructionText() // Update text
    }

    private fun hidePlanes() {
        _state.update { it.copy(showPlaneRenderer = false) }
    }


    // --- Helper to update instruction text based on state ---
    private fun updateInstructionText() {
        val currentState = _state.value // Get current state once
        val text = when {
            currentState.trackingFailureReason != null -> "Tracking Lost: ${currentState.trackingFailureReason}" // Consider using getDescription() here
            currentState.availableItems.isEmpty() && !currentState.isLoadingCollection -> "No items in collection"
            currentState.selectedItemModelFile != null -> "Tap a surface to place the item"
            !currentState.showPlaneRenderer -> "Tap to place another item or move existing ones" // After first placement
            else -> "Point phone down to find a surface" // Default initial state
        }
        _state.update { it.copy(instructionText = text) }
    }

}