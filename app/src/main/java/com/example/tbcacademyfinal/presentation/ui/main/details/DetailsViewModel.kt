package com.example.tbcacademyfinal.presentation.ui.main.details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.tbcacademyfinal.common.Resource
import com.example.tbcacademyfinal.domain.usecase.collection.AddToCollectionUseCase
import com.example.tbcacademyfinal.domain.usecase.collection.IsItemInCollectionUseCase
import com.example.tbcacademyfinal.domain.usecase.products.GetProductDetailsUseCase
import com.example.tbcacademyfinal.presentation.mapper.toDomainModel
import com.example.tbcacademyfinal.presentation.mapper.toUiModel
import com.example.tbcacademyfinal.presentation.navigation.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
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

    var state by mutableStateOf(DetailsState())
        private set

    private val _event = MutableSharedFlow<DetailsSideEffect>()
    val event: SharedFlow<DetailsSideEffect> = _event.asSharedFlow()

    init {
        fetchProductDetails()
        observeCollectionStatus()
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
                        state = state.copy(isLoading = true, error = null)
                    }

                    is Resource.Success -> {
                        state =
                            state.copy(
                                isLoading = false,
                                product = resource.data.toUiModel(),
                                error = null
                            )
                    }


                    is Resource.Error -> {
                        state = state.copy(isLoading = false, error = resource.message)
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

    private fun observeCollectionStatus() {
        viewModelScope.launch {
            isItemInCollectionUseCase(productId)
                .distinctUntilChanged() // Only react to actual changes
                .collect { isInCollection ->
                    state = state.copy(isAddedToCollection = isInCollection)
                }
        }
    }

    private fun addToCollection() {
        val currentDomainProduct = state.product


        viewModelScope.launch {
            if (currentDomainProduct == null) {
                _event.emit(DetailsSideEffect.ShowError("Product data not loaded yet."))
                return@launch
            }
            if (state.isAddedToCollection) return@launch

            val result =
                addToCollectionUseCase(currentDomainProduct.toDomainModel())
            when (result) {
                is Resource.Success -> {
                    // State will update automatically via observeCollectionStatus
                    _event.emit(DetailsSideEffect.ShowAddedToCollectionMessage)
                }

                is Resource.Error -> {
                    _event.emit(DetailsSideEffect.ShowError(result.message))
                }

                is Resource.Loading -> {}
            }
        }
    }

    private fun emitNavigateBack() {
        viewModelScope.launch {
            _event.emit(DetailsSideEffect.NavigateBack)
        }
    }
}