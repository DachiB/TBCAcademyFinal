package com.example.tbcacademyfinal.presentation.ui.main.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.tbcacademyfinal.domain.model.Product
import com.example.tbcacademyfinal.domain.usecase.collection.AddToCollectionUseCase
import com.example.tbcacademyfinal.domain.usecase.collection.IsItemInCollectionUseCase
import com.example.tbcacademyfinal.domain.usecase.products.GetProductDetailsUseCase
import com.example.tbcacademyfinal.presentation.mapper.toUiModel
import com.example.tbcacademyfinal.presentation.navigation.Routes
import com.example.tbcacademyfinal.common.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val getProductDetailsUseCase: GetProductDetailsUseCase,
    private val addToCollectionUseCase: AddToCollectionUseCase, // Inject Add UC
    private val isItemInCollectionUseCase: IsItemInCollectionUseCase, // Inject Check UC
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val productId: String = savedStateHandle.toRoute<Routes.DetailsRoute>().productId

    private var domainProduct: Product? = null

    private val _state = MutableStateFlow(DetailsState())
    val state: StateFlow<DetailsState> = _state.asStateFlow()

    private val _event = MutableSharedFlow<DetailsSideEffect>()
    val event: SharedFlow<DetailsSideEffect> = _event.asSharedFlow()

    init {
        fetchProductDetails()
        observeCollectionStatus() // Start observing if item is in collection
    }

    fun processIntent(intent: DetailsIntent) {
        when (intent) {
            is DetailsIntent.AddToCollectionClicked -> addToCollection()
            is DetailsIntent.NavigateBack -> emitNavigateBack()
            is DetailsIntent.ClickedModel -> navigateToModelScreen(intent.productId)
        }
    }

    private fun fetchProductDetails() {
        viewModelScope.launch {
            getProductDetailsUseCase(productId).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _state.update { it.copy(isLoading = true, error = null) }
                    }

                    is Resource.Success -> {
                        domainProduct = resource.data // Store the domain product
                        _state.update {
                            it.copy(
                                isLoading = false,
                                product = resource.data.toUiModel(),
                                error = null
                            )
                        }
                        // isAddedToCollection state is now handled by observeCollectionStatus
                    }

                    is Resource.Error -> {
                        _state.update { it.copy(isLoading = false, error = resource.message) }
                    }
                }
            }
        }
    }

    private fun navigateToModelScreen(productId: String) {
        viewModelScope.launch {
            _event.emit(DetailsSideEffect.NavigateToModel(productId))
        }
    }

    // Observe if the current product ID exists in the collection
    private fun observeCollectionStatus() {
        viewModelScope.launch {
            isItemInCollectionUseCase(productId)
                .distinctUntilChanged() // Only react to actual changes
                .collect { isInCollection ->
                    _state.update { it.copy(isAddedToCollection = isInCollection) }
                }
        }
    }

    private fun addToCollection() {
        val currentDomainProduct = domainProduct // Use stored domain product
        if (currentDomainProduct == null) {
            _event.tryEmit(DetailsSideEffect.ShowError("Product data not loaded yet."))
            return
        }

        viewModelScope.launch {
            // Prevent adding if already added (UI should disable button, but double check)
            if (state.value.isAddedToCollection) return@launch

            val result = addToCollectionUseCase(currentDomainProduct) // Pass domain product
            when (result) {
                is Resource.Success -> {
                    // State will update automatically via observeCollectionStatus
                    _event.tryEmit(DetailsSideEffect.ShowAddedToCollectionMessage)
                }

                is Resource.Error -> {
                    _event.tryEmit(DetailsSideEffect.ShowError(result.message))
                }

                is Resource.Loading -> { /* Optional: Handle loading state for add */
                }
            }
        }
    }

    private fun emitNavigateBack() {
        _event.tryEmit(DetailsSideEffect.NavigateBack)
    }
}