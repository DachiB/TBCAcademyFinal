package com.example.tbcacademyfinal.presentation.ui.main.collection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tbcacademyfinal.domain.usecase.collection.ClearCollectionUseCase
import com.example.tbcacademyfinal.domain.usecase.collection.GetCollectionItemsUseCase
import com.example.tbcacademyfinal.domain.usecase.collection.RemoveFromCollectionUseCase
import com.example.tbcacademyfinal.presentation.mapper.toUiModelList
import com.example.tbcacademyfinal.common.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CollectionViewModel @Inject constructor(
    private val getCollectionItemsUseCase: GetCollectionItemsUseCase,
    private val removeFromCollectionUseCase: RemoveFromCollectionUseCase,
    private val clearCollectionUseCase: ClearCollectionUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(CollectionState())
    val state: StateFlow<CollectionState> = _state.asStateFlow()

    private val _event = MutableSharedFlow<CollectionSideEffect>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val event: SharedFlow<CollectionSideEffect> = _event.asSharedFlow()

    init {
        loadCollectionItems()
    }

    fun processIntent(intent: CollectionIntent) {
        when (intent) {
            is CollectionIntent.LoadCollection -> loadCollectionItems() // Allow refresh
            is CollectionIntent.RemoveItem -> removeItem(intent.productId)
            is CollectionIntent.ClearCollection -> clearAllItems()
            is CollectionIntent.StartDecorating -> startAr()
        }
    }

    private fun loadCollectionItems() {
        viewModelScope.launch {
            getCollectionItemsUseCase()
                .onStart { _state.update { it.copy(isLoading = true, error = null) } }
                .catch { e ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = e.localizedMessage ?: "Failed to load collection"
                        )
                    }
                }
                .collect { domainItems ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            items = domainItems.toUiModelList(),
                            error = null
                        )
                    }
                }
        }
    }

    private fun removeItem(productId: String) {
        viewModelScope.launch {
            val result = removeFromCollectionUseCase(productId)
            // State list will update automatically via the flow from getCollectionItemsUseCase
            if (result is Resource.Error) {
                _event.tryEmit(CollectionSideEffect.ShowError(result.message))
            }
        }
    }

    private fun clearAllItems() {
        viewModelScope.launch {
            val result = clearCollectionUseCase()
            // State list will update automatically via the flow
            if (result is Resource.Error) {
                _event.tryEmit(CollectionSideEffect.ShowError(result.message))
            }
        }
    }

    private fun startAr() {
        _event.tryEmit(CollectionSideEffect.NavigateToArScreen)
    }
}
