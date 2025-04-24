package com.example.tbcacademyfinal.presentation.ui.main.collection

import androidx.compose.runtime.Immutable
import com.example.tbcacademyfinal.presentation.model.CollectionItemUi

@Immutable
data class CollectionState(
    val isLoading: Boolean = true,
    val items: List<CollectionItemUi> = emptyList(),
    val error: String? = null
)