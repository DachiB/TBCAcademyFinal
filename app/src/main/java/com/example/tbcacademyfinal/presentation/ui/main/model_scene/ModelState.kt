package com.example.tbcacademyfinal.presentation.ui.main.model_scene

import androidx.compose.runtime.Immutable
import com.example.tbcacademyfinal.presentation.model.ProductUi

@Immutable
data class ModelState(
    val isLoading: Boolean = true,
    val product: ProductUi? = null,
    val error: String? = null,
)