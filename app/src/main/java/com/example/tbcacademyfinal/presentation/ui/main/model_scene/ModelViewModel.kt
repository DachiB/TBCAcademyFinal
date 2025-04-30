package com.example.tbcacademyfinal.presentation.ui.main.model_scene

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.tbcacademyfinal.common.Resource
import com.example.tbcacademyfinal.domain.usecase.products.GetProductDetailsUseCase
import com.example.tbcacademyfinal.presentation.mapper.toUiModel
import com.example.tbcacademyfinal.presentation.navigation.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ModelViewModel @Inject constructor(
    private val getProductDetailsUseCase: GetProductDetailsUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val productId: String = savedStateHandle.toRoute<Routes.ModelRoute>().productId

    var state by mutableStateOf(ModelState())
        private set

    private val _event = MutableSharedFlow<ModelSideEffect>()
    val event: SharedFlow<ModelSideEffect> = _event.asSharedFlow()

    init {
        fetchProductDetails()
    }

    fun processIntent(intent: ModelIntent) {
        when (intent) {
            ModelIntent.ClickedBack -> {
                viewModelScope.launch { _event.emit(ModelSideEffect.NavigateBack) }
            }
        }
    }

    private fun fetchProductDetails() {
        viewModelScope.launch {
            getProductDetailsUseCase(productId).collect { resource ->
                state = when (resource) {
                    is Resource.Loading -> {
                        state.copy(isLoading = true, error = null)
                    }

                    is Resource.Success -> {
                        state.copy(
                            isLoading = false,
                            product = resource.data.toUiModel(),
                            error = null
                        )
                    }

                    is Resource.Error -> {
                        state.copy(isLoading = false, error = resource.message)
                    }
                }
            }
        }
    }
}