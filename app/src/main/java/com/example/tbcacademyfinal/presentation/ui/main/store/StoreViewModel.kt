package com.example.tbcacademyfinal.presentation.ui.main.store

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tbcacademyfinal.common.Resource
import com.example.tbcacademyfinal.domain.model.Product
import com.example.tbcacademyfinal.domain.usecase.products.GetProductsUseCase
import com.example.tbcacademyfinal.presentation.mapper.toUiModelList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoreViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase
) : ViewModel() {

    // Hold the raw domain products fetched from the repository
    private val _rawProductsFlow = MutableStateFlow<Resource<List<Product>>>(Resource.Loading)

    // Hold the search query separately
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Combine raw products and search query to produce the final UI state
    val state: StateFlow<StoreState> = combine(
        _rawProductsFlow,
        _searchQuery
    ) { productResource, query ->
        when (productResource) {
            is Resource.Loading -> StoreState(isLoading = true)
            is Resource.Error -> StoreState(isLoading = false, error = productResource.message)
            is Resource.Success -> {
                val allProducts = productResource.data
                val filteredProducts = if (query.isBlank()) {
                    allProducts // No filter if query is blank
                } else {
                    allProducts.filter {
                        // Simple filter: check name or description (case-insensitive)
                        it.name.contains(query, ignoreCase = true) ||
                                it.description.contains(query, ignoreCase = true)
                        // Add category filter later if needed
                    }
                }
                StoreState(
                    isLoading = false,
                    products = filteredProducts.toUiModelList(), // Map *filtered* list to UI models
                    searchQuery = query // Reflect current query in state
                )
            }
        }
    }.stateIn( // Convert the combined Flow into a StateFlow
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000), // Keep active while UI is visible
        initialValue = StoreState(isLoading = true) // Initial state while fetching
    )


    private val _event = MutableSharedFlow<StoreSideEffect>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val event: SharedFlow<StoreSideEffect> = _event.asSharedFlow()

    init {
        fetchProducts() // Trigger initial fetch
    }

    fun processIntent(intent: StoreIntent) {
        when (intent) {
            is StoreIntent.LoadProducts -> fetchProducts() // Allow explicit refresh
            is StoreIntent.ProductClicked -> navigateToDetails(intent.productId)
            is StoreIntent.SearchQueryChanged -> _searchQuery.value =
                intent.query // Update search query flow
        }
    }

    // Fetches from repository and updates the private _rawProductsFlow
    private fun fetchProducts() {
        // Prevent refetch if already loading/success unless forced refresh
        // if (_rawProductsFlow.value !is Resource.Loading && intent is not forced) return

        viewModelScope.launch {
            getProductsUseCase().collect { resource ->
                _rawProductsFlow.value = resource // Update the raw data flow
            }
        }
    }

    private fun navigateToDetails(productId: String) {
        _event.tryEmit(StoreSideEffect.NavigateToDetails(productId))
    }
}