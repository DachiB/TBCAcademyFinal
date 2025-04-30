package com.example.tbcacademyfinal.presentation.ui.main.store

sealed interface StoreSideEffect {
    data class NavigateToDetails(val productId: String) : StoreSideEffect
    data class ShowErrorSnackbar(val message: String) : StoreSideEffect
}