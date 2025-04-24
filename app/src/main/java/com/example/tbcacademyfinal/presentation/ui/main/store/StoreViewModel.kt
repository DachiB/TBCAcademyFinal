package com.example.tbcacademyfinal.presentation.ui.main.store

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tbcacademyfinal.domain.usecase.products.GetProductsUseCase
import com.example.tbcacademyfinal.presentation.mapper.toUiModelList
import com.example.tbcacademyfinal.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoreViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(StoreState())
    val state: StateFlow<StoreState> = _state.asStateFlow() // Expose as StateFlow

    private val _event = MutableSharedFlow<StoreSideEffect>(
        replay = 0, extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val event: SharedFlow<StoreSideEffect> = _event.asSharedFlow() // Expose as SharedFlow

    init {
        // Automatically load products when ViewModel is created
        processIntent(StoreIntent.LoadProducts)
    }

    fun processIntent(intent: StoreIntent) {
        when (intent) {
            is StoreIntent.LoadProducts -> fetchProducts()
            is StoreIntent.ProductClicked -> navigateToDetails(intent.productId)
        }
    }

    private fun fetchProducts() {
        // Prevent reloading if already loading
        if (_state.value.isLoading && _state.value.products.isNotEmpty()) return // Avoid reload if loading for the first time already

        viewModelScope.launch {
            getProductsUseCase().collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _state.update { it.copy(isLoading = true, error = null) }
                    }

                    is Resource.Success -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                // Map domain models to UI models here
                                products = resource.data.toUiModelList(),
                                error = null
                            )
                        }
                    }

                    is Resource.Error -> {
                        _state.update { it.copy(isLoading = false, error = resource.message) }
                        // Optionally emit a snackbar event
                        // _event.tryEmit(StoreSideEffect.ShowErrorSnackbar(resource.message))
                    }
                }
            }
        }
    }

    private fun navigateToDetails(productId: String) {
        // Emit side effect for navigation
        _event.tryEmit(StoreSideEffect.NavigateToDetails(productId))
    }
}