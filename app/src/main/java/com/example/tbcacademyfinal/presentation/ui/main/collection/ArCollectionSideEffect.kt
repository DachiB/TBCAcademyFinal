package com.example.tbcacademyfinal.presentation.ui.main.collection

sealed interface ArCollectionSideEffect {
    data class ShowError(val message: String) : ArCollectionSideEffect
    data object NavigateToArScreen : ArCollectionSideEffect // Navigate to the actual AR view
}