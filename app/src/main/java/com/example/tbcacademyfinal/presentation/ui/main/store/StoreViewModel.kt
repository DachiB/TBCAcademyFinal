package com.example.tbcacademyfinal.presentation.ui.main.store

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tbcacademyfinal.common.Resource
import com.example.tbcacademyfinal.domain.usecase.collection.AddToCollectionUseCase
import com.example.tbcacademyfinal.domain.usecase.collection.IsItemInCollectionUseCase
import com.example.tbcacademyfinal.domain.usecase.network.ObserveNetworkStatusUseCase
import com.example.tbcacademyfinal.domain.usecase.products.GetProductsUseCase
import com.example.tbcacademyfinal.domain.repository.ConnectivityObserver
import com.example.tbcacademyfinal.presentation.mapper.toDomainModel
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
    private val observeNetworkStatusUseCase: ObserveNetworkStatusUseCase,
    private val addToCollectionUseCase: AddToCollectionUseCase,
    private val isItemInCollectionUseCase: IsItemInCollectionUseCase
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

            is StoreIntent.HasModelOnlyChanged -> {
                state = state.copy(hasModelOnly = intent.hasModel)
                updateFilters()
            }

            is StoreIntent.PriceRangeChanged -> {
                state = state.copy(minPrice = intent.min, maxPrice = intent.max)
                updateFilters()
            }

            is StoreIntent.FilterButtonClicked -> state =
                state.copy(isFiltering = intent.isFiltering)

            StoreIntent.ClickedAddDailyCollection -> addDailyCollection()
            StoreIntent.ClickedAddItemOfDay -> addDailyItem()
        }
    }

    private fun updateFilters() {
        val query = state.searchQuery.trim()
        val category = state.selectedCategory
        val hasModelOnly = state.hasModelOnly
        val (min, max) = Pair(state.minPrice, state.maxPrice)

        val filtered = state.allProducts.filter { product ->
            val matchesText = query.isBlank() || product.name.contains(query, ignoreCase = true)
            val matchesCat = category == null || product.category == category
            val matchesPrice = product.rawPrice.toFloat() in min..max
            val matchesModel = !hasModelOnly || product.modelFile.isNotBlank()

            matchesText && matchesCat && matchesPrice && matchesModel
        }

        val filteringActive =
            query.isNotBlank() ||
                    category != null ||
                    hasModelOnly ||
                    min > 0f ||
                    max < 10000f

        state = state.copy(currentProducts = filtered, isFiltering = filteringActive)
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
                        val list = resource.data.toUiModelList()

                        val prices = list.map { it.rawPrice.toFloat() }
                        val minP = prices.minOrNull() ?: 0f
                        val maxP = prices.maxOrNull() ?: minP

                        val daily = list.shuffled().take(7)
                        val itemOfDay = list.randomOrNull()

                        itemOfDay?.id?.let { id ->
                            viewModelScope.launch {
                                isItemInCollectionUseCase(id)
                                    .collect { inCollection ->
                                        state = state.copy(isDailyItemInCollection = inCollection)
                                    }
                            }
                        }

                        state.copy(
                            isLoading = false,
                            error = null,
                            allProducts = list,
                            currentProducts = list,
                            availableCategories = list.map { it.category }
                                .filter { it.isNotBlank() }
                                .distinct()
                                .sorted(),
                            minPrice = minP,
                            maxPrice = maxP,
                            dailyCollection = daily,
                            dailyItem = itemOfDay
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

    private fun addDailyCollection() = viewModelScope.launch {
        val items = state.dailyCollection
        var successCount = 0

        items.forEach { ui ->
            val domain = ui.toDomainModel()
            val result = addToCollectionUseCase(domain)
            if (result is Resource.Error) {
                _event.emit(StoreSideEffect.ShowErrorSnackbar(result.message))
            } else {
                successCount++
            }
        }

        if (successCount > 0) {
            _event.emit(
                StoreSideEffect.ShowErrorSnackbar(
                    "$successCount item${if (successCount > 1) "s" else ""} added to collection"
                )
            )
        }
    }

    private fun addDailyItem() {
        viewModelScope.launch {
            state.dailyItem?.let { product ->
                val result = addToCollectionUseCase(product.toDomainModel())
                if (result is Resource.Error) {
                    _event.emit(StoreSideEffect.ShowErrorSnackbar(result.message))
                } else {
                    _event.emit(StoreSideEffect.ShowErrorSnackbar("Added to collection"))
                }
            }

        }
    }
}