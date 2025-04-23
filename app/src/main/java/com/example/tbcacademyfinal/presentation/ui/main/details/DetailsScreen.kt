package com.example.tbcacademyfinal.presentation.ui.main.details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun DetailsScreen(productId: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Details for Product ID: $productId\n(Click Back to return)")
    }
    // Add BackHandler or use navController.popBackStack() on a button etc.
}