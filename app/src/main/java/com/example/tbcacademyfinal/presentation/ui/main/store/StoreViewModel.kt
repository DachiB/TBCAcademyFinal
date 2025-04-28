package com.example.tbcacademyfinal.presentation.ui.main.store

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tbcacademyfinal.common.Resource
import com.example.tbcacademyfinal.domain.usecase.network.ObserveNetworkStatusUseCase
import com.example.tbcacademyfinal.domain.usecase.products.GetProductsUseCase
import com.example.tbcacademyfinal.domain.util.ConnectivityObserver
import com.example.tbcacademyfinal.presentation.mapper.toUiModelList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoreViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    private val observeNetworkStatusUseCase: ObserveNetworkStatusUseCase
) : ViewModel() {

    var state by mutableStateOf(StoreState())
        private set

    private val _event = MutableSharedFlow<StoreSideEffect>()
    var event = _event.asSharedFlow()

    private var searchJob: Job? = null

    init {
        observeConnectivity()
    }

    fun processIntent(intent: StoreIntent) {
        when (intent) {
            is StoreIntent.LoadProducts -> observeConnectivity()
            is StoreIntent.ProductClicked -> navigateToDetails(intent.productId)
            is StoreIntent.SearchQueryChanged -> {
                state = state.copy(searchQuery = intent.query)
                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    state = state.copy(isSearching = true)
                    delay(500)
                    updateFilters()
                    state = state.copy(isSearching = false)
                }
            }

            is StoreIntent.RetryButtonClicked -> {
                observeConnectivity()
            }

            is StoreIntent.CategorySelected -> {
                state = state.copy(selectedCategory = intent.category)
                updateFilters()
            }

            is StoreIntent.ClearCategoryFilter -> {
                state = state.copy(selectedCategory = null)
                updateFilters()
            }
        }
    }

    private fun updateFilters() {
        val query = state.searchQuery.trim()
        val category = state.selectedCategory

        val filtered = state.allProducts.filter { product ->
            (query.isBlank() || product.name.contains(query, ignoreCase = true)) &&
                    (category == null || product.category == category)
        }

        state = state.copy(currentProducts = filtered)
    }


    private fun navigateToDetails(productId: String) {
        viewModelScope.launch {
            _event.emit(StoreSideEffect.NavigateToDetails(productId))
        }
    }


    private fun fetchProducts() {
        viewModelScope.launch {
            getProductsUseCase().collect { resource ->
                state = when (resource) {
                    is Resource.Loading -> state.copy(isLoading = true)
                    is Resource.Error -> {
                        state.copy(isLoading = false, error = resource.message)
                    }

                    is Resource.Success -> {
                        val productList = resource.data.toUiModelList()
                        val categoryList = productList
                            .map { it.category }
                            .filter { it.isNotBlank() }
                            .distinct()
                            .sorted()

                        state.copy(
                            isLoading = false,
                            error = null,
                            allProducts = productList,
                            currentProducts = productList,
                            availableCategories = categoryList
                        )
                    }
                }
            }
        }
    }

    private fun observeConnectivity() {
        viewModelScope.launch {
            observeNetworkStatusUseCase().collect { status ->
                state = when (status) {
                    ConnectivityObserver.Status.Available -> {
                        fetchProducts()
                        Log.d("NetworkStatus", "Available")
                        state.copy(isNetworkAvailable = true)
                    }

                    ConnectivityObserver.Status.Lost -> {
                        viewModelScope.launch {
                            _event.emit(StoreSideEffect.ShowErrorSnackbar("No Internet Connection"))
                        }
                        state.copy(isNetworkAvailable = false)

                    }

                    ConnectivityObserver.Status.Unavailable -> {
                        viewModelScope.launch {
                            _event.emit(StoreSideEffect.ShowErrorSnackbar("No Internet Connection"))
                        }
                        state.copy(isNetworkAvailable = false)
                    }

                    ConnectivityObserver.Status.Losing -> {
                        Log.d("NetworkStatus", "Losing")
                        state.copy(isNetworkAvailable = false)
                    }
                }
            }
        }
    }


}