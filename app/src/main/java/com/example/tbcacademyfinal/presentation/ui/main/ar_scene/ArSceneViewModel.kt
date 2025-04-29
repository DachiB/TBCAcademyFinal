package com.example.tbcacademyfinal.presentation.ui.main.ar_scene

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tbcacademyfinal.domain.usecase.collection.GetCollectionItemsUseCase
import com.example.tbcacademyfinal.presentation.mapper.toUiModelList
import com.example.tbcacademyfinal.presentation.model.CollectionItemUi
import com.google.ar.core.TrackingFailureReason
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArSceneViewModel @Inject constructor(
    private val getCollectionItemsUseCase: GetCollectionItemsUseCase
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
        }
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

                    _event.emit(ArSceneSideEffect.ShowError(errorMsg))
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

}