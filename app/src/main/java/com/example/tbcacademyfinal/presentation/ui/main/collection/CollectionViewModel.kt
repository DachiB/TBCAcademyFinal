package com.example.tbcacademyfinal.presentation.ui.main.collection

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tbcacademyfinal.domain.usecase.collection.ClearCollectionUseCase
import com.example.tbcacademyfinal.domain.usecase.collection.GetCollectionItemsUseCase
import com.example.tbcacademyfinal.domain.usecase.collection.RemoveFromCollectionUseCase
import com.example.tbcacademyfinal.presentation.mapper.toUiModelList
import com.example.tbcacademyfinal.common.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
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

    var state by mutableStateOf(CollectionState())
        private set

    private val _event = MutableSharedFlow<CollectionSideEffect>()
    val event: SharedFlow<CollectionSideEffect> = _event.asSharedFlow()

    init {
        loadCollectionItems()
    }

    fun processIntent(intent: CollectionIntent) {
        when (intent) {
            is CollectionIntent.LoadCollection -> loadCollectionItems()
            is CollectionIntent.RemoveItem -> removeItem(intent.productId)
            is CollectionIntent.ClearCollection -> clearAllItems()
            is CollectionIntent.StartDecorating -> startAr()
            is CollectionIntent.BuyCollection -> TODO()
        }
    }

    private fun loadCollectionItems() {
        viewModelScope.launch {
            getCollectionItemsUseCase()
                .onStart {
                    state = state.copy(isLoading = true, error = null)
                }
                .catch { e ->
                    state = state.copy(isLoading = false, error = e.message)
                }
                .collect { domainItems ->
                    state = state.copy(
                        isLoading = false,
                        error = null,
                        items = domainItems.toUiModelList()
                    )
                }
        }
    }

    private fun removeItem(productId: String) {
        viewModelScope.launch {
            val result = removeFromCollectionUseCase(productId)
            if (result is Resource.Error) {
                viewModelScope.launch {
                    _event.emit(CollectionSideEffect.ShowError(result.message))
                }
            }
        }
    }

    private fun clearAllItems() {
        viewModelScope.launch {
            val result = clearCollectionUseCase()
            if (result is Resource.Error) {
                viewModelScope.launch {
                    _event.emit(CollectionSideEffect.ShowError(result.message))
                }
            }
        }
    }

    private fun startAr() {
        viewModelScope.launch {
            _event.emit(CollectionSideEffect.NavigateToArScreen)
        }
    }
}
