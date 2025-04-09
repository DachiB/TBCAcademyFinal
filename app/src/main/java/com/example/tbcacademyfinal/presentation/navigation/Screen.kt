package com.example.tbcacademyfinal.presentation.navigation

import androidx.compose.ui.graphics.vector.ImageVector


sealed class Screen(val route: String) {
    data object ProductList : Screen("product_list")
    data object ARPreview : Screen("ar_preview")
}

data class ProductNavigationItem(
    val destination: Screen,
    val label: String,
    val icon: ImageVector
)