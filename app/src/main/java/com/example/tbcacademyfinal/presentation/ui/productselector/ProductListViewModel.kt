package com.example.tbcacademyfinal.presentation.ui.productselector

// presentation/viewmodel/ProductListViewModel.kt

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tbcacademyfinal.domain.model.ProductDomain
import com.example.tbcacademyfinal.domain.usecase.GetProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase
) : ViewModel() {

    var productList by mutableStateOf(emptyList<ProductDomain>())
        private set

    init {
        viewModelScope.launch {
            productList = getProductsUseCase()
        }
    }
}
